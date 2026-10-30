package org.boxutil.base.api.container;

import org.boxutil.util.collection.longtype.LongArrayDeque;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public interface QueueExt<E> extends Queue<E>, Container {
    int DEFAULT_INITIAL_CAPACITY = (1 << 4) + 1;

    default E element() {
        E result;
        if ((result = this.peek()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    default boolean offer(E e) {
        return this.add(e);
    }

    default E remove() {
        E result;
        if ((result = this.poll()) == this.getNullElement()) throw new NoSuchElementException();
        return result;
    }

    /**
     * Attempts to poll an element from the head of this queue, perform the given
     * action on it, and then offer the element back to the tail of this queue.
     *
     * <p>This method is non-blocking. If this queue is empty, or if
     * {@link #poll()} returns {@code null}, this method immediately returns
     * {@code false} and the given action is not performed.
     *
     * <p>If the given action throws an exception, the element is not offered back
     * to this queue and the exception is propagated to the caller.
     *
     * @implSpec
     * The default implementation is equivalent to:
     * <pre>{@code
     * if (isEmpty()) return false;
     * E e = poll();
     * if (e != null) {
     *     action.accept(e);
     *     offer(e);
     * }
     * return e != null;
     * }</pre>
     * The default implementation ignores the return value of
     * {@link #offer(Object)}; if this queue rejects the element, the element is
     * not offered back. The default implementation does not guarantee atomicity;
     * the behavior of concurrent invocations depends on the queue implementation.
     *
     * @param action the action to perform on the polled element
     * @return {@code true} if an element was polled, the action was performed, and
     *         the element was offered back; {@code false} if this queue is empty or
     *         {@code poll()} returns {@code null}
     * @throws NullPointerException if {@code action} is {@code null}
     */
    default boolean tryPollExecuteOffer(@NotNull Consumer<? super E> action) {
        Objects.requireNonNull(action);

        if (this.isEmpty()) return false;
        E element;
        if ((element = this.poll()) != null) {
            action.accept(element);
            this.offer(element);
        }
        return element != null;
    }

    /**
     * Swaps the elements between this queue and the specified queue. After this
     * operation, this queue contains the elements previously held by the
     * specified queue, and the specified queue contains the elements previously
     * held by this queue. In each case, the elements are held in the same
     * head-to-tail order in which they were held by the other queue. If the
     * specified queue is the same as this queue, the operation has no effect.
     *
     * <p>The specified queue must be compatible with this queue such that all
     * elements can be exchanged without altering their values.
     *
     * <p>If either this queue or the specified queue has a bounded capacity, the
     * number of elements held by the other queue may exceed that capacity. In
     * such a case, this operation may fail without modifying either queue.
     *
     * <p>Unless {@code source} is this queue, this operation is a structural
     * modification. Iterators, spliterators, and other traversal objects
     * previously obtained from either queue are considered invalid after this
     * operation; their subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}. Previously obtained
     * views may remain associated with the queue object, but their validity,
     * contents, and binding relationship after the swap are
     * implementation-specific.
     *
     * <p><b>Implementation Note:</b> Implementations may perform the swap by
     * exchanging internal data structures directly instead of transferring
     * elements one by one. In that case, references to internal data obtained
     * through implementation-specific means (if any) may observe the contents
     * now held by the other queue, and previously obtained views or iterators
     * may be invalidated. This interface does not specify the identity,
     * validity, or fail-fast behavior of such views, iterators, or internal
     * references after a swap.
     *
     * @implSpec
     * The default implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param source the queue whose elements are to be exchanged with the
     *        elements of this queue
     * @throws NullPointerException if the specified queue is {@code null}
     * @throws UnsupportedOperationException if the swap operation is not
     *         supported by this queue or the specified queue; an implementation
     *         that performs the swap using basic mutation operations may also
     *         throw if those operations are unsupported
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of an element in the specified
     *         queue prevents it from being stored in this queue, or vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of an element in the
     *         specified queue prevents it from being stored in this queue, or
     *         vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalStateException if either this queue or the specified queue
     *         has a bounded capacity and cannot accommodate the elements held by
     *         the other queue
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void swap(@NotNull Queue<E> source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the element that this queue uses to represent a null element.
     *
     * <p>Some queue implementations do not permit {@code null} elements to be
     * stored, but still need a way to represent an empty, absent, or placeholder
     * value. Such implementations may accept a custom "null element" at
     * construction time, which this method returns. An implementation may also
     * accept {@code null} itself as its null element, in which case this method
     * returns {@code null}.
     *
     * <p>Callers can use the returned object to determine whether a value held by
     * the queue represents an empty value, for example by comparing with
     * {@link Object#equals(Object)} or reference equality ({@code ==}). Whether
     * and how the returned element is used by this queue is implementation-specific;
     * this method only exposes the null representation specified at construction
     * time and imposes no additional constraints on the insertion, removal, or
     * comparison behavior of the queue.
     *
     * <p>The default implementation returns {@code null}, indicating that this
     * queue does not define a custom null element and uses {@code null} itself as
     * its null representation. In that case, callers cannot distinguish "no null
     * element defined" from "the null element is {@code null}" through this
     * method; the two have the same meaning.
     *
     * @implSpec
     * The default implementation returns {@code null}. Implementations are
     * encouraged to return the same object instance that was specified at
     * construction time, so that callers can compare it using reference equality
     * ({@code ==}). If the returned object is mutable, this queue makes no
     * guarantees about its state; modifying the object may affect the queue's
     * recognition of its null element.
     *
     * @return the element that this queue uses to represent a null element, or
     *         {@code null} if this queue uses {@code null} itself as its null
     *         element or does not define a null element
     */
    default E getNullElement() {
        return null;
    }

    /**
     * Compares the contents of this queue with the specified collection for
     * equality.
     *
     * <p>Returns {@code true} if the specified collection is also a {@link Queue}
     * and both queues contain equal elements in the same iteration order. Returns
     * {@code false} if the specified collection is {@code null}, is not a
     * {@code Queue}, or does not contain equal elements in the same iteration
     * order.
     *
     * @implSpec
     * The default implementation first checks whether {@code source} is the same
     * object as this queue. If so, it returns {@code true}. Otherwise, if
     * {@code source} is not a {@code Queue}, it returns {@code false}. If
     * {@code source} is a {@code Queue}, it compares the sizes of the two queues
     * and then compares their elements pairwise, in iteration order, using
     * {@link Objects#equals(Object, Object)}.
     *
     * @param source the collection to compare with this queue
     * @return {@code true} if {@code source} is a {@code Queue} containing equal
     *         elements to this queue in the same iteration order; {@code false}
     *         otherwise
     * @see Object#equals(Object)
     */
    default boolean contentEquals(Collection<?> source) {
        if (source == this) return true;

        if (source instanceof Queue<?>) {
            Queue<?> castQueue = (Queue<?>) source;
            if (castQueue.size() != this.size()) return false;
            Iterator<E> currIt = this.iterator();
            for (Object o : castQueue) {
                if (!(currIt.hasNext() && Objects.equals(currIt.next(), o))) return false;
            }
            return true;
        }
        return false;
    }
}
