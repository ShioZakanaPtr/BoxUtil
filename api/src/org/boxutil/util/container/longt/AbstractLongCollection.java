package org.boxutil.util.container.longt;

import org.boxutil.base.api.container.longt.LongCollection;
import org.boxutil.base.api.container.longt.LongIterator;
import org.boxutil.base.api.container.longt.LongSpliterator;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;

public abstract class AbstractLongCollection extends AbstractCollection<Long> implements LongCollection {
    protected AbstractLongCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(long o) {
        final LongIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextLong() == o) return true;
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean contains(final Object o) {
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

    public boolean remove(long o) {
        final LongIterator it = this.iterator();
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
    public boolean remove(final Object o) {
        if (o != null && !(o instanceof Long)) return false;
        return this.remove(o != null ? ((Long) o).longValue() : (long) 0);
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
    public abstract LongIterator iterator();

    @NotNull
    public abstract LongSpliterator spliterator();

    public void clear() {
        final LongIterator it = this.iterator();
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
