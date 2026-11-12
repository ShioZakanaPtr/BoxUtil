package io.github.shiozakanaptr.acext.api.collection;

/**
 * A collection that has a well-defined encounter order and supports operations
 * at both ends.
 *
 * <p>This interface exists to provide a uniform, forward-compatible view of the
 * first/last element operations introduced in JDK 21 as part of the
 * {@code SequencedCollection} hierarchy[reference:0]. Developers targeting older
 * JDK versions can program against this interface to ease future migration,
 * while developers on newer JDK versions are ensured that calls resolve to the
 * correct implementation for the underlying collection type via method signature.
 *
 * <p>When a collection implements this interface, callers should consult the
 * corresponding method documentation in JDK 21's
 * <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html">
 * {@code SequencedCollection}</a> interface for the full contract, including
 * encounter-order semantics and optional-operation behavior.
 *
 * @param <E> the type of elements in this collection
 * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedCollection.html">
 *      SequencedCollection (JDK 21)</a>
 * @since 1.0.0
 */
public interface OrderedCollection<E> {
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
}
