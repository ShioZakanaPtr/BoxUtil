package org.boxutil.util.container.intt;

import org.boxutil.base.api.container.intt.IntCollection;
import org.boxutil.base.api.container.intt.IntIterator;
import org.boxutil.base.api.container.intt.IntSpliterator;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class AbstractIntCollection extends AbstractCollection<Integer> implements IntCollection {
    protected AbstractIntCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(int o) {
        final IntIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextInt() == o) return true;
        }
        return false;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean contains(final Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.contains(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    public boolean add(int e) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    public boolean add(final Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
    }

    public boolean remove(int o) {
        final IntIterator it = this.iterator();
        while (it.hasNext()) {
            if (it.nextInt() == o) {
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
        if (o != null && !(o instanceof Integer)) return false;
        return this.remove(o != null ? ((Integer) o).intValue() : (int) 0);
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
    public abstract IntIterator iterator();

    @NotNull
    public abstract IntSpliterator spliterator();

    public void clear() {
        final IntIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextInt();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
