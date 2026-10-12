package org.boxutil.util.container.objectt;

import org.boxutil.base.api.container.longt.LongCollection;
import org.boxutil.base.api.container.longt.LongIterator;
import org.boxutil.base.api.container.longt.LongSpliterator;
import org.boxutil.base.api.container.objectt.ObjCollection;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;

public abstract class AbstractObjCollection<E> extends AbstractCollection<E> implements ObjCollection<E> {
    protected AbstractObjCollection() {}

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

    public abstract int size();

    public boolean isEmpty() {
        return this.size() < 1;
    }

    @NotNull
    public abstract Iterator<E> iterator();

    @NotNull
    public abstract Spliterator<E> spliterator();

    public void clear() {
        final Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }
}
