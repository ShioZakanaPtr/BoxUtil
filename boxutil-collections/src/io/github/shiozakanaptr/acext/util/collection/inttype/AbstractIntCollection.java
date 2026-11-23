package io.github.shiozakanaptr.acext.util.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntIterator;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSpliterator;

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
        IntIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextInt()) return true;
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.contains(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    public boolean add(int e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Integer e) {
        return this.add(e != null ? e.intValue() : (int) 0);
    }

    public boolean removeElement(int o) {
        IntIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextInt()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeElement(Object o) {
        if (o != null && !(o instanceof Integer)) return false;
        return this.removeElement(o != null ? ((Integer) o).intValue() : (int) 0);
    }

    public boolean remove(Object o) {
        return this.removeElement(o);
    }

    public boolean containsAll(Collection<?> c) {
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

    public abstract IntIterator iterator();

    public abstract IntSpliterator spliterator();

    public void clear() {
        IntIterator it = this.iterator();
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
