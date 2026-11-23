package io.github.shiozakanaptr.acext.util.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleIterator;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSpliterator;

import java.util.*;

public abstract class AbstractDoubleCollection extends AbstractCollection<Double> implements DoubleCollection {
    protected AbstractDoubleCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(double o) {
        DoubleIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextDouble()) return true;
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.contains(o != null ? ((Double) o).doubleValue() : (double) 0);
    }

    public boolean add(double e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Double e) {
        return this.add(e != null ? e.doubleValue() : (double) 0);
    }

    public boolean removeElement(double o) {
        DoubleIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextDouble()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeElement(Object o) {
        if (o != null && !(o instanceof Double)) return false;
        return this.removeElement(o != null ? ((Double) o).doubleValue() : (double) 0);
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

    public abstract DoubleIterator iterator();

    public abstract DoubleSpliterator spliterator();

    public void clear() {
        DoubleIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextDouble();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
