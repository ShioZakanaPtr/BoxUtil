package io.github.shiozakanaptr.acext.api.collection;

/**
 * A set that has a well-defined encounter order, supports operations at
 * both ends, and provides bidirectional iterators.
 *
 * <p>This interface combines the element-uniqueness constraints of a
 * {@link SetExt} with the ordered, two-ended view and bidirectional iteration
 * provided by {@link OrderedCollection}. The encounter order of an
 * {@code OrderedSet} is the order in which its elements are encountered during
 * iteration; the first/last element operations and the iterators inherited
 * from {@link OrderedCollection} are defined with respect to that encounter
 * order.
 *
 * <p>This interface exists to provide a uniform, forward-compatible view of
 * the first/last element operations introduced in JDK 21 as part of the
 * {@code SequencedCollection} hierarchy for ordered sets, together with
 * bidirectional iteration via {@link OrderedCollection#iterator()} and
 * {@link OrderedCollection#iteratorReversed()}. Developers targeting older JDK
 * versions can program against this interface to ease future migration, while
 * developers on newer JDK versions are ensured that calls resolve to the
 * correct implementation for the underlying set type via method signature. In
 * intent, this interface corresponds to JDK 21's {@code SequencedSet}, while
 * retaining the {@link SetExt} contract.
 *
 * <p>When a set implements this interface, callers should consult the
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
 * Callers requiring a reversed view should use the underlying set's JDK 21
 * {@code SequencedSet} API directly, when available.
 *
 * @param <E> the type of elements in this set
 *
 * @see OrderedCollection
 * @see SetExt
 * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedSet.html">
 *      SequencedSet (JDK 21)</a>
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface OrderedSet<E> extends OrderedCollection<E>, SetExt<E> {
}
