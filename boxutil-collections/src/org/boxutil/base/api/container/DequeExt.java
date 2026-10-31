package org.boxutil.base.api.container;

import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

public interface DequeExt<E> extends Deque<E>, QueueExt<E> {
    default E element() {
        E result;
        if ((result = this.peek()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    // For compatibility
    default E getFirst() {
        E result;
        if ((result = this.peekFirst()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    // For compatibility
    default E getLast() {
        E result;
        if ((result = this.peekLast()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    default boolean add(E e) {
        this.addLast(e);
        return true;
    }

    // For compatibility
    default void addFirst(E e) {
        throw new UnsupportedOperationException();
    }

    // For compatibility
    default void addLast(E e) {
        throw new UnsupportedOperationException();
    }

    default boolean offer(E e) {
        this.addLast(e);
        return true;
    }

    default boolean offerFirst(E e) {
        this.addFirst(e);
        return true;
    }

    default boolean offerLast(E e) {
        this.addLast(e);
        return true;
    }

    default E remove() {
        E result;
        if ((result = this.poll()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    // For compatibility
    default E removeFirst() {
        E result;
        if ((result = this.pollFirst()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    // For compatibility
    default E removeLast() {
        E result;
        if ((result = this.pollLast()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    default void push(E e) {
        this.addFirst(e);
    }

    default E pop() {
        return this.pollFirst();
    }

    /**
     * Attempts to poll an element from the tail of this deque, perform the given
     * action on it, and then offer the element back to the head of this deque.
     *
     * <p>This method is the reverse-direction counterpart of
     * {@link #tryPollExecuteOffer(Consumer)}: it polls from the tail and offers
     * back to the head. This method is non-blocking. If this deque is empty, or if
     * {@link #pollLast()} returns {@code null}, this method immediately returns
     * {@code false} and the given action is not performed.
     *
     * <p>If the given action throws an exception, the element is not offered back
     * to this deque and the exception is propagated to the caller.
     *
     * @implSpec
     * The default implementation is equivalent to:
     * <pre>{@code
     * if (isEmpty()) return false;
     * E e = pollLast();
     * if (e != null) {
     *     action.accept(e);
     *     offerFirst(e);
     * }
     * return e != null;
     * }</pre>
     * The default implementation ignores the return value of
     * {@link #offerFirst(Object)}; if this deque rejects the element, the element
     * is not offered back. The default implementation does not guarantee
     * atomicity; the behavior of concurrent invocations depends on the deque
     * implementation.
     *
     * @param action the action to perform on the polled element
     * @return {@code true} if an element was polled, the action was performed, and
     *         the element was offered back; {@code false} if this deque is empty or
     *         {@code pollLast()} returns {@code null}
     * @throws NullPointerException if {@code action} is {@code null}
     */
    default boolean tryPollExecuteOfferReversed(@NotNull Consumer<? super E> action) {
        Objects.requireNonNull(action);

        if (this.isEmpty()) return false;
        E element;
        if ((element = this.pollLast()) != null) {
            action.accept(element);
            this.offerFirst(element);
        }
        return element != null;
    }

    /**
     * Swaps the elements between this deque and the specified deque. After this
     * operation, this deque contains the elements previously held by the
     * specified deque, and the specified deque contains the elements previously
     * held by this deque. In each case, the elements are held in the same
     * first-to-last order in which they were held by the other deque. If the
     * specified deque is the same as this deque, the operation has no effect.
     *
     * <p>The specified deque must be compatible with this deque such that all
     * elements can be exchanged without altering their values.
     *
     * <p>If either this deque or the specified deque has a bounded capacity, the
     * number of elements held by the other deque may exceed that capacity. In
     * such a case, this operation may fail without modifying either deque.
     *
     * <p>Unless {@code source} is this deque, this operation is a structural
     * modification. Iterators, spliterators, and other traversal objects
     * previously obtained from either deque are considered invalid after this
     * operation; their subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}. Previously obtained
     * views may remain associated with the deque object, but their validity,
     * contents, and binding relationship after the swap are
     * implementation-specific.
     *
     * <p><b>Implementation Note:</b> Implementations may perform the swap by
     * exchanging internal data structures directly instead of transferring
     * elements one by one. In that case, references to internal data obtained
     * through implementation-specific means (if any) may observe the contents
     * now held by the other deque, and previously obtained views or iterators
     * may be invalidated. This interface does not specify the identity,
     * validity, or fail-fast behavior of such views, iterators, or internal
     * references after a swap.
     *
     * @implSpec
     * The default implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param source the deque whose elements are to be exchanged with the
     *        elements of this deque
     * @throws NullPointerException if the specified deque is {@code null}
     * @throws UnsupportedOperationException if the swap operation is not
     *         supported by this deque or the specified deque; an implementation
     *         that performs the swap using basic mutation operations may also
     *         throw if those operations are unsupported
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of an element in the specified
     *         deque prevents it from being stored in this deque, or vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified deque prevents it from being stored in this deque, or
     *         vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalStateException if either this deque or the specified deque
     *         has a bounded capacity and cannot accommodate the elements held by
     *         the other deque
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void swap(@NotNull Deque<E> source) {
        throw new UnsupportedOperationException();
    }
}
