package org.boxutil.util.collection.objecttype;

import org.boxutil.base.api.container.objecttype.ObjCollection;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

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
