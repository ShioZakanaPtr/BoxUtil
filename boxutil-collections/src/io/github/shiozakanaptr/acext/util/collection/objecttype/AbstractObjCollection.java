package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;

import java.util.*;

public abstract class AbstractObjCollection<E> extends AbstractCollection<E> implements ObjCollection<E> {
    protected AbstractObjCollection() {}

    public void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    public void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(final Object o) {
        final Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (Objects.equals(o, it.next())) return true;
        }
        return false;
    }

    public boolean add(final E e) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        final Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (Objects.equals(o, it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
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

    public abstract Iterator<E> iterator();

    public abstract Spliterator<E> spliterator();

    public void clear() {
        final Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public void clear(int newCapacity) {
        this.clear();
        this.resize(newCapacity);
    }
}
