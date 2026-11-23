package io.github.shiozakanaptr.acext.util.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatIterator;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatSpliterator;

import java.util.*;

public abstract class AbstractFloatCollection extends AbstractCollection<Float> implements FloatCollection {
    protected AbstractFloatCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(float o) {
        FloatIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextFloat()) return true;
        }
        return false;
    }

    public boolean contains(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.contains(o != null ? ((Float) o).floatValue() : (float) 0);
    }

    public boolean add(float e) {
        throw new UnsupportedOperationException();
    }

    public boolean add(Float e) {
        return this.add(e != null ? e.floatValue() : (float) 0);
    }

    public boolean removeElement(float o) {
        FloatIterator it = this.iterator();
        while (it.hasNext()) {
            if (o == it.nextFloat()) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean removeElement(Object o) {
        if (o != null && !(o instanceof Float)) return false;
        return this.removeElement(o != null ? ((Float) o).floatValue() : (float) 0);
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

    public abstract FloatIterator iterator();

    public abstract FloatSpliterator spliterator();

    public void clear() {
        FloatIterator it = this.iterator();
        while (it.hasNext()) {
            it.nextFloat();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
