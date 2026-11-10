package io.github.shiozakanaptr.acext.api.collection;

public interface OrderedCollection<E> {
    // For compatibility
    E getFirst();

    // For compatibility
    E getLast();

    // For compatibility
    void addFirst(E e);

    // For compatibility
    void addLast(E e);

    // For compatibility
    E removeFirst();

    // For compatibility
    E removeLast();
}
