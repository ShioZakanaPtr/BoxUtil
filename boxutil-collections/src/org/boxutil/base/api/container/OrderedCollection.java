package org.boxutil.base.api.container;

public interface OrderedCollection<E> {
    E getFirst();

    E getLast();

    void addFirst(E e);

    void addLast(E e);

    E removeFirst();

    E removeLast();
}
