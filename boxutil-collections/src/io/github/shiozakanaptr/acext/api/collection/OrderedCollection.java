package io.github.shiozakanaptr.acext.api.collection;

public interface OrderedCollection<E> {
    E getFirst();

    E getLast();

    void addFirst(E e);

    void addLast(E e);

    E removeFirst();

    E removeLast();
}
