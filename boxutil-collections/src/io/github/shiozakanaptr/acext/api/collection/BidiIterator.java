package io.github.shiozakanaptr.acext.api.collection;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An {@link Iterator} that additionally supports bidirectional,
 * element-by-element traversal.
 *
 * <p>Unlike {@link java.util.ListIterator}, this interface does not require the
 * underlying collection to be a {@code List}, nor does it expose indexing or
 * mutation operations. The traversal order is defined by the iterator
 * implementation.
 *
 * <p>The iterator's cursor is positioned between elements.
 * {@link Iterator#next()} returns the element after the cursor and moves the
 * cursor forward; {@link #previous()} returns the element before the cursor and
 * moves the cursor backward. Calls to {@code next()} and {@code previous()} may
 * be interleaved.
 *
 * <p>The inherited {@link Iterator} contracts for {@code next()},
 * {@code remove()}, and {@code forEachRemaining(Consumer)} continue to apply.
 * Implementations may support or reject {@code remove()} according to the
 * optional-operation contract of {@code Iterator}.
 *
 * @param <E> the type of elements returned by this iterator
 *
 * @see Iterator
 * @see java.util.ListIterator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface BidiIterator<E> extends Iterator<E> {
    /**
     * Returns the next element in the iteration and moves the cursor forward.
     *
     * <p>This method may be called repeatedly to traverse forward, and may be
     * interleaved with calls to {@link #previous()}, but same as
     * {@link java.util.ListIterator}, will return the same element repeatedly.
     *
     * @return the next element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no next
     *         element (i.e. {@link Iterator#hasNext()} ()} returns {@code false})
     */
    E next();

    /**
     * Returns {@code true} if this iterator has more elements when traversed
     * in the reverse direction.
     *
     * <p>More precisely, returns {@code true} if {@link #previous()} would
     * return an element rather than throw {@link java.util.NoSuchElementException}.
     *
     * @return {@code true} if {@link #previous()} would return an element;
     *         {@code false} otherwise
     */
    boolean hasPrevious();

    /**
     * Returns the previous element in the iteration and moves the cursor
     * backward.
     *
     * <p>This method may be called repeatedly to traverse backward, and may be
     * interleaved with calls to {@link #next()}, but same as
     * {@link java.util.ListIterator}, will return the same element repeatedly.
     *
     * <p>The traversal order is the reverse of the order in which {@code next()}
     * would return elements for this iterator.
     *
     * @return the previous element in the iteration
     * @throws java.util.NoSuchElementException if the iteration has no previous
     *         element (i.e. {@link #hasPrevious()} returns {@code false})
     */
    E previous();

    /**
     * Performs the given action for each remaining element, traversing
     * backward, until all elements have been processed or the action throws an
     * exception.
     *
     * <p>The default implementation repeatedly calls {@link #hasPrevious()}
     * and {@link #previous()} until {@code hasPrevious()} returns
     * {@code false}. Actions are performed in reverse traversal order, i.e. in
     * the order in which {@link #previous()} returns elements.
     *
     * @param action the action to be performed for each element
     * @throws NullPointerException if {@code action} is {@code null}
     */
    default void forEachPrevious(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        while (this.hasPrevious()) {
            action.accept(this.previous());
        }
    }
}
