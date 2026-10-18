package org.boxutil.base.api.container;

import java.util.NoSuchElementException;

public interface SequencedCollectionExt<E> extends CollectionExt<E> {
    /**
     * Gets the first element of this collection.
     *
     * @implSpec
     * The implementation in this interface obtains an iterator of this collection, and
     * then it obtains an element by calling the iterator's {@code next} method. Any
     * {@code NoSuchElementException} thrown is propagated. Otherwise, it returns
     * the element.
     *
     * @return the retrieved element
     * @throws NoSuchElementException if this collection is empty
     */
    E getFirst();

    /**
     * Gets the last element of this collection.
     *
     * @implSpec
     * The implementation in this interface obtains an iterator of the reversed view
     * of this collection, and then it obtains an element by calling the iterator's
     * {@code next} method. Any {@code NoSuchElementException} thrown is propagated.
     * Otherwise, it returns the element.
     *
     * @return the retrieved element
     * @throws NoSuchElementException if this collection is empty
     */
    E getLast();

    /**
     * Adds an element as the first element of this collection (optional operation).
     * After this operation completes normally, the given element will be a member of
     * this collection, and it will be the first element in encounter order.
     *
     * @implSpec
     * The implementation in this interface always throws {@code UnsupportedOperationException}.
     *
     * @param e the element to be added
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     * @throws UnsupportedOperationException if this collection implementation
     *         does not support this operation
     */
    default void addFirst(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds an element as the last element of this collection (optional operation).
     * After this operation completes normally, the given element will be a member of
     * this collection, and it will be the last element in encounter order.
     *
     * @implSpec
     * The implementation in this interface always throws {@code UnsupportedOperationException}.
     *
     * @param e the element to be added.
     * @throws NullPointerException if the specified element is null and this
     *         collection does not permit null elements
     * @throws UnsupportedOperationException if this collection implementation
     *         does not support this operation
     */
    default void addLast(E e) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes and returns the first element of this collection (optional operation).
     *
     * @implSpec
     * The implementation in this interface obtains an iterator of this collection, and then
     * it obtains an element by calling the iterator's {@code next} method. Any
     * {@code NoSuchElementException} thrown is propagated. It then calls the iterator's
     * {@code remove} method. Any {@code UnsupportedOperationException} thrown is propagated.
     * Then, it returns the element.
     *
     * @return the removed element
     * @throws NoSuchElementException if this collection is empty
     * @throws UnsupportedOperationException if this collection implementation
     *         does not support this operation
     */
    E removeFirst();

    /**
     * Removes and returns the last element of this collection (optional operation).
     *
     * @implSpec
     * The implementation in this interface obtains an iterator of the reversed view of this
     * collection, and then it obtains an element by calling the iterator's {@code next} method.
     * Any {@code NoSuchElementException} thrown is propagated. It then calls the iterator's
     * {@code remove} method. Any {@code UnsupportedOperationException} thrown is propagated.
     * Then, it returns the element.
     *
     * @return the removed element
     * @throws NoSuchElementException if this collection is empty
     * @throws UnsupportedOperationException if this collection implementation
     *         does not support this operation
     */
    E removeLast();
}
