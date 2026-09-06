package org.boxutil.backends.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class BUtil_AtomicEnumSet32<E extends Enum<E>> {
    protected final byte maxElements;
    protected final AtomicInteger elements = new AtomicInteger();
    protected final E[] elementsArray;

    /**
     * max 32 enum.
     */
    public BUtil_AtomicEnumSet32(final Class<E> elementType) {
        this.elementsArray = elementType.getEnumConstants();
        this.maxElements = (byte) this.elementsArray.length;
    }

    public boolean add(final E e) {
        final int pos = 1 << e.ordinal();
        int oldValue, newValue;
        do {
            oldValue = this.elements.get();
            if ((oldValue & pos) != 0) return false;
            newValue = oldValue | pos;
        } while (!this.elements.compareAndSet(oldValue, newValue));
        return true;
    }

    public boolean remove(final E e) {
        final int pos = 1 << e.ordinal();
        int oldValue, newValue;
        do {
            oldValue = this.elements.get();
            if ((oldValue & pos) == 0) return false;
            newValue = oldValue & ~pos;
        } while (!this.elements.compareAndSet(oldValue, newValue));
        return true;
    }

    public void clear() {
        this.elements.set(0);
    }

    public boolean contains(final E e) {
        return (this.elements.get() & (1 << e.ordinal())) != 0;
    }

    public void forEach(final Consumer<E> doEach) {
        final int snapshot = this.elements.get();
        int pos = 1;
        byte getEnum = 0;
        do {
            if ((snapshot & pos) != 0) doEach.accept(this.elementsArray[getEnum]);
            pos <<= 1;
        } while ((getEnum++) < this.maxElements);
    }
}
