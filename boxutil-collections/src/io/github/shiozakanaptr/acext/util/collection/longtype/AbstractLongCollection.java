package io.github.shiozakanaptr.acext.util.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.longtype.LongCollection;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIterator;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSpliterator;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Objects;

public abstract class AbstractLongCollection extends AbstractCollection<Long> implements LongCollection {
    protected AbstractLongCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(long o) {
        LongIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextLong() == o) return true;
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean contains(Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.contains(o != null ? ((Long) o).longValue() : (long) 0);
    }

    public boolean add(long e) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean add(final Long e) {
        return this.add(e != null ? e.longValue() : (int) 0);
    }

    public boolean removeElement(long o) {
        LongIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextLong() == o) {
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
        if (o != null && !(o instanceof Long)) return false;
        return this.removeElement(o != null ? ((Long) o).longValue() : (long) 0);
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

    public abstract LongIterator iterator();

    public abstract LongSpliterator spliterator();

    public void clear() {
        LongIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextLong();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
