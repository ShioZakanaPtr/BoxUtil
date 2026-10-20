package org.boxutil.util.container.objecttype;

import org.boxutil.base.api.container.longtype.LongIterator;
import org.boxutil.base.api.container.objecttype.ObjList;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Spliterator;

public abstract class AbstractObjList<E> extends AbstractObjCollection<E> implements ObjList<E> {
    protected AbstractObjList() {}

    public abstract E get(int index);

    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(final E e) {
        this.add(this.size(), e);
        return true;
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public E removeAt(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeElement(Object o) {
        final Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (it.next() == o) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int indexOf(Object o) {
        ListIterator<E> it = this.listIterator();
        while (it.hasNext()) {
            if (Objects.equals(o, it.next())) {
                return it.previousIndex();
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        ListIterator<E> it = this.listIterator(this.size());
        while (it.hasPrevious()) {
            if (Objects.equals(o, it.previous())) {
                return it.nextIndex();
            }
        }
        return -1;
    }
}
