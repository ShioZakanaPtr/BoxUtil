package io.github.shiozakanaptr.acext.api.collection;

/**
 * An {@link EntrySetExt} view of the mappings contained in a map that has a
 * well-defined encounter order, supports operations at both ends, and provides
 * bidirectional iterators.
 *
 * <p>This interface combines the entry-view semantics of {@link EntrySetExt}
 * with the ordered, two-ended view and bidirectional iteration provided by
 * {@link OrderedSet}. The encounter order of an {@code OrderedEntrySet} is the
 * order in which its entries are encountered during iteration; the first/last
 * element operations and the iterators inherited from {@link OrderedCollection}
 * are defined with respect to that encounter order.
 *
 * <p>This interface is intended to be usable by all {@link OrderedMap}
 * implementations whose entry sets have a well-defined encounter order. As with
 * {@link EntrySetExt}, the ordinary {@link SetExt} traversal methods, including
 * {@link #forEach(java.util.function.Consumer)}, {@link #iterator()}, and
 * {@link #spliterator()}, retain the usual {@code Set} contract: entries
 * returned by them may be stored and used after traversal. The {@code view*}
 * methods are additional, opt-in alternatives for performance-sensitive code
 * that does not retain entries. Entries supplied by {@code view*} methods are
 * transient views valid only as specified by the individual method, and the
 * caller must not store them, share them across threads, or rely on their
 * identity, {@link Object#hashCode()}, or {@link Object#equals(Object)}.
 *
 * <p>This interface exists to provide a uniform, forward-compatible view of the
 * first/last element operations introduced in JDK 21 as part of the
 * {@code SequencedCollection} hierarchy for ordered entry sets, together with
 * bidirectional iteration via {@link OrderedCollection#iterator()} and
 * {@link OrderedCollection#iteratorReversed()}. Developers targeting older JDK
 * versions can program against this interface to ease future migration, while
 * developers on newer JDK versions are ensured that calls resolve to the
 * correct implementation for the underlying entry set type via method
 * signature. In intent, this interface corresponds to JDK 21's
 * {@code SequencedSet} for map entry sets, while retaining the
 * {@link EntrySetExt} contract.
 *
 * <p>When an entry set implements this interface, callers should consult the
 * corresponding method documentation in JDK 21's
 * <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedSet.html">
 * {@code SequencedSet}</a> interface for the full contract, including
 * encounter-order semantics, element uniqueness, and optional-operation
 * behavior. The bidirectional iterator methods are specified by
 * {@link OrderedCollection}.
 *
 * <p><b>Note:</b> This interface does not adapt the
 * {@code reversed()} method of {@code SequencedSet}. It provides first/last
 * element operations and bidirectional iterators, but
 * {@link OrderedCollection#iteratorReversed()} does not return a reversed view.
 * Callers requiring a reversed view should use the underlying entry set's JDK 21
 * {@code SequencedSet} API directly, when available.
 *
 * @implNote
 * Implementations must keep the ordinary {@code Set} traversal methods
 * unchanged. The {@code view*} methods are additional opt-in methods and do
 * not weaken the contract of {@code Set}.
 *
 * @param <E> the element type; for the entry set of a map this is normally
 *            {@code MapExt.Entry<K,V>}
 *
 * @see OrderedSet
 * @see EntrySetExt
 * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedSet.html">
 *      SequencedSet (JDK 21)</a>
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
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
