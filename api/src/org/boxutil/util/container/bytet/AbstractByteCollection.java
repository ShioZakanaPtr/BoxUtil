package org.boxutil.util.container.bytet;

import org.boxutil.base.api.container.bytet.ByteCollection;
import org.boxutil.base.api.container.bytet.ByteIterator;
import org.boxutil.base.api.container.bytet.ByteSpliterator;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;

public abstract class AbstractByteCollection extends AbstractCollection<Byte> implements ByteCollection {
    protected AbstractByteCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(byte o) {
        final ByteIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextByte() == o) return true;
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean contains(final Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.contains(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    public boolean add(byte e) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean add(final Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    public boolean remove(byte o) {
        final ByteIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextByte() == o) {
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
        if (o != null && !(o instanceof Byte)) return false;
        return this.remove(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    public int getCapacity() {
        return -1;
    }

    public int getRemainingSpace() {
        return -1;
    }

    public abstract int size();

    public boolean isEmpty() {
        return this.size() < 1;
    }

    @NotNull
    public abstract ByteIterator iterator();

    @NotNull
    public abstract ByteSpliterator spliterator();

    public void clear() {
        final ByteIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextByte();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
