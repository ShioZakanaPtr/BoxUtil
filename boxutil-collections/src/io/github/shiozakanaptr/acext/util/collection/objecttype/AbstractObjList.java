package io.github.shiozakanaptr.acext.util.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BidiIterator;
import io.github.shiozakanaptr.acext.api.collection.objecttype.*;

import java.util.*;

public abstract class AbstractObjList<E> extends AbstractObjCollection<E> implements ObjList<E> {
    protected AbstractObjList() {}

    public void shrink() {
        this.resize(0);
    }

    public abstract E get(int index);

    public E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    public boolean add(E e) {
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
        BidiIterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (Objects.equals(o, it.next())) {
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

    public abstract BidiIterator<E> iterator();

    public abstract BidiIterator<E> iteratorReversed();

    public abstract Spliterator<E> spliterator();
}
