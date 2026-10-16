package org.boxutil.util.container.shortt;

import org.boxutil.base.api.container.shortt.ShortCollection;
import org.boxutil.base.api.container.shortt.ShortIterator;
import org.boxutil.base.api.container.shortt.ShortSpliterator;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;

public abstract class AbstractShortCollection extends AbstractCollection<Short> implements ShortCollection {
    protected AbstractShortCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(short o) {
        final ShortIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextShort() == o) return true;
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean contains(final Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.contains(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    public boolean add(short e) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean add(final Short e) {
        return this.add(e != null ? e.shortValue() : (short) 0);
    }

    public boolean remove(short o) {
        final ShortIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextShort() == o) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean remove(final Object o) {
        if (o != null && !(o instanceof Short)) return false;
        return this.remove(o != null ? ((Short) o).shortValue() : (short) 0);
    }

    public int capacity() {
        return -1;
    }

    public int remaining() {
        return -1;
    }

    public abstract int size();

    public boolean isEmpty() {
        return this.size() < 1;
    }

    @NotNull
    public abstract ShortIterator iterator();

    @NotNull
    public abstract ShortSpliterator spliterator();

    public void clear() {
        final ShortIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextShort();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
