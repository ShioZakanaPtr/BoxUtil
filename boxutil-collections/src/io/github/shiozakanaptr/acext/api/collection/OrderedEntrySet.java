package io.github.shiozakanaptr.acext.api.collection;

public interface OrderedEntrySet<E> extends EntrySetExt<E>, OrderedSet<E> {
    /**
     * Returns a bidirectional iterator over the entries in this set, in reverse
     * encounter order, that may reuse an implementation-internal entry
     * instance.
     *
     * <p>The traversal order is the reverse of the order used by
     * {@link #viewIterator()}, and is the same as the order of
     * {@link #iteratorReversed()}. The iterator is initially positioned after
     * the last entry in encounter order; the first call to
     * {@link BidiIterator#previous()} returns the last entry and begins reverse
     * traversal. The first call to {@link BidiIterator#next()} throws
     * {@link java.util.NoSuchElementException} until {@code previous()} has
     * been called at least once.
     *
     * <p>The {@link BidiIterator#next()}, {@link BidiIterator#previous()},
     * {@link BidiIterator#forEachRemaining(java.util.function.Consumer)}, and
     * {@link BidiIterator#forEachPrevious(java.util.function.Consumer)} methods
     * may return or pass the same entry instance on successive elements. An
     * entry returned by {@code next()} or {@code previous()} is valid only
     * until the next call to {@code next()}, {@code previous()},
     * {@code forEachRemaining()}, {@code forEachPrevious()}, or
     * {@code remove()} on the returned iterator. An entry passed to the action
     * of {@code forEachRemaining()} or {@code forEachPrevious()} is valid only
     * during that action invocation.
     *
     * @implSpec
     * The returned iterator must traverse the same entries in the same order as
     * {@link #iteratorReversed()}. Implementations should reuse an internal
     * entry instance and may allocate a new entry only when reuse is not
     * possible. The caller must nevertheless treat every returned entry as
     * transient.
     *
     * @return a bidirectional iterator over this set's entries in reverse
     *         encounter order, whose entries are transient views; use
     *         {@link #iteratorReversed()} if entries must be retained
     *
     * @apiNote
     * Do not store an entry returned by this iterator. To retain an entry,
     * copy its key and value or use {@link #iteratorReversed()}.
     */
    BidiIterator<E> viewIteratorReversed();
}
