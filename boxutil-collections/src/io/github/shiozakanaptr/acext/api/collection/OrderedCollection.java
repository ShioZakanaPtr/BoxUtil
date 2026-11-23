package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;

/**
 * A collection that has a well-defined encounter order, supports operations
 * at both ends, and provides bidirectional iterators.
 *
 * <p>This interface extends {@link CollectionExt} and refines
 * {@link #iterator()} to return a {@link BidiIterator}, while additionally
 * providing {@link #iteratorReversed()} for reverse traversal. The
 * {@code iterator()} method is a covariant override of
 * {@link CollectionExt#iterator()}; {@code iteratorReversed()} is an additional
 * operation not present in {@link CollectionExt}.
 *
 * <p>This interface exists to provide a uniform, forward-compatible view of the
 * first/last element operations introduced in JDK 21 as part of the
 * {@code SequencedCollection} hierarchy, together with bidirectional iteration
 * via {@link #iterator()} and {@link #iteratorReversed()}. Developers targeting
 * older JDK versions can program against this interface to ease future
 * migration, while developers on newer JDK versions are ensured that calls
 * resolve to the correct implementation for the underlying collection type via
 * method signature.
 *
 * <p>When a collection implements this interface, callers should consult the
 * corresponding method documentation in JDK 21's
 * <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html">
 * {@code SequencedCollection}</a> interface for the full contract, including
 * encounter-order semantics and optional-operation behavior. The iterator
 * methods are specified by this interface: {@link #iterator()} covariantly
 * overrides {@link CollectionExt#iterator()}, and {@link #iteratorReversed()} is
 * an additional method not present in {@link CollectionExt}.
 *
 * <p><b>Note:</b> This interface does not adapt the
 * {@code reversed()} method of {@code SequencedCollection}. It provides
 * first/last element operations and bidirectional iterators, but
 * {@link #iteratorReversed()} does not return a reversed view. Callers
 * requiring a reversed view should use the underlying collection's JDK 21
 * {@code SequencedCollection} API directly, when available.
 *
 * @param <E> the type of elements in this collection
 *
 * @see BidiIterator
 * @see CollectionExt
 * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html">
 *      SequencedCollection (JDK 21)</a>
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface OrderedCollection<E> extends CollectionExt<E> {
    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html#getFirst()">
     *      SequencedCollection.getFirst()</a>
     */
    E getFirst();

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html#getLast()">
     *      SequencedCollection.getLast()</a>
     */
    E getLast();

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html#addFirst(E)">
     *      SequencedCollection.addFirst(E)</a>
     */
    void addFirst(E e);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html#addLast(E)">
     *      SequencedCollection.addLast(E)</a>
     */
    void addLast(E e);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html#removeFirst()">
     *      SequencedCollection.removeFirst()</a>
     */
    E removeFirst();

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html#removeLast()">
     *      SequencedCollection.removeLast()</a>
     */
    E removeLast();

    /**
     * {@inheritDoc}
     *
     * <p>This method is overridden to return a {@link BidiIterator}, which
     * supports traversal in both directions. The forward direction of the
     * returned iterator, and therefore the element returned by its first
     * {@code next()} call, is the same as specified by
     * {@link CollectionExt#iterator()}: iteration starts at the first element in
     * this collection's encounter order and proceeds toward the last element.
     *
     * <p>Since the iterator starts before the first element, the first call to
     * {@link BidiIterator#previous()} throws
     * {@link java.util.NoSuchElementException}, and
     * {@link BidiIterator#hasPrevious()} returns {@code false} until at least
     * one forward step has been taken.
     *
     * @return a {@link BidiIterator} over the elements of this collection,
     *         starting at the first element in encounter order
     */
    @NotNull
    BidiIterator<E> iterator();

    /**
     * Returns a bidirectional iterator over the elements of this collection in
     * reverse encounter order.
     *
     * <p>The returned iterator is the counterpart of {@link #iterator()}:
     * whereas {@code iterator()} starts before the first element and moves
     * toward the last, {@code iteratorReversed()} starts after the last element
     * and moves toward the first. Accordingly, reverse traversal can begin
     * immediately: the first call to {@link BidiIterator#previous()} returns the
     * last element of this collection, and each subsequent {@code previous()}
     * call returns the element preceding the one just returned.
     *
     * <p>Because the iterator is initially positioned after the last element,
     * {@link BidiIterator#hasNext()} returns {@code false} and the first call to
     * {@link BidiIterator#next()} throws {@link java.util.NoSuchElementException}.
     * This remains the case until {@link BidiIterator#previous()} has been called
     * at least once. After at least one {@code previous()} call, {@code next()}
     * becomes available and moves in the original, non-reversed encounter order,
     * returning the element most recently returned by {@code previous()}.
     *
     * <p>If this collection is empty, both {@link BidiIterator#hasNext()} and
     * {@link BidiIterator#hasPrevious()} return {@code false}, and both
     * {@code next()} and {@code previous()} throw
     * {@link java.util.NoSuchElementException}.
     *
     * <p>This method does not return a reversed view of this collection; it
     * merely returns an iterator whose initial position and {@code previous()}
     * direction allow reverse traversal from the last element.
     *
     * @return a {@link BidiIterator} over the elements of this collection,
     *         initially positioned after the last element in encounter order,
     *         so that the first {@code previous()} call returns the last element
     * @throws java.util.NoSuchElementException if {@code next()} is called
     *         before {@code previous()} has been called at least once
     */
    @NotNull
    BidiIterator<E> iteratorReversed();
}
