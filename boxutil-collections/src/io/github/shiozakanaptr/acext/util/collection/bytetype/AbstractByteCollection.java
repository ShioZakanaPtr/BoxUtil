package io.github.shiozakanaptr.acext.util.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIterator;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSpliterator;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Objects;

public abstract class AbstractByteCollection extends AbstractCollection<Byte> implements ByteCollection {
    protected AbstractByteCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(byte o) {
        ByteIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextByte() == o) return true;
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean contains(Object o) {
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
    public boolean add(Byte e) {
        return this.add(e != null ? e.byteValue() : (byte) 0);
    }

    public boolean removeElement(byte o) {
        ByteIterator it = this.iterator();
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
    public boolean removeElement(Object o) {
        if (o != null && !(o instanceof Byte)) return false;
        return this.removeElement(o != null ? ((Byte) o).byteValue() : (byte) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean remove(Object o) {
        return this.removeElement(o);
    }

    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        if (c == this) return true;
        if (this.isEmpty()) return c.isEmpty();
        return super.containsAll(c);
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

    public abstract ByteIterator iterator();

    public abstract ByteSpliterator spliterator();

    public void clear() {
        ByteIterator it = this.iterator();
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
