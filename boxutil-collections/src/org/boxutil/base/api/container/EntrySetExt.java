package org.boxutil.base.api.container;

import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A {@link SetExt} view of the mappings contained in a map, extended with
 * traversal methods that may reuse an implementation-internal entry
 * instance.
 *
 * <p>This interface is intended to be usable by all {@link Map}
 * implementations. The ordinary {@link SetExt} methods, including
 * {@link #forEach(java.util.function.Consumer)},
 * {@link #iterator()}, and {@link #spliterator()}, retain the usual
 * {@code Set} contract: entries returned by them may be stored and used
 * after traversal. The {@code view*} methods are additional, opt-in
 * alternatives for performance-sensitive code that does not retain
 * entries. They may pass an implementation-internal, reused entry to the
 * caller in order to avoid allocating a new entry for each element.
 *
 * <p>An entry supplied by a {@code view*} method is a
 * <em>transient view</em>. It is valid only during the current callback
 * or until the iterator or spliterator is advanced, as specified by the
 * individual method. The caller must not:
 * <ul>
 *   <li>store the entry reference beyond that point;</li>
 *   <li>place the entry in a collection or use it as a map key;</li>
 *   <li>rely on its identity, {@link Object#hashCode()}, or
 *       {@link Object#equals(Object)};</li>
 *   <li>share the entry, the returned iterator, or the returned
 *       spliterator across threads; or</li>
 * </ul>
 *
 * <p>To retain an entry, copy its key and value, for example with
 * {@code new java.util.AbstractMap.SimpleImmutableEntry<>(key, value)},
 * or use the ordinary {@link #iterator()},
 * {@link #forEach(java.util.function.Consumer)}, or
 * {@link #spliterator()} methods. The effect of calling
 * {@link Map.Entry#setValue(Object)} on a reused entry is
 * implementation-specific unless the implementation documents otherwise.
 *
 * @implNote
 * Implementations must keep the ordinary {@code Set} traversal methods
 * unchanged. The {@code view*} methods are additional opt-in methods and
 * do not weaken the contract of {@code Set}.
 *
 * @param <E> the element type; for the entry set of a map this is
 *            normally {@code Map.Entry<K,V>}
 *
 * @see Map#entrySet()
 */
public interface EntrySetExt<E> extends SetExt<E> {
    /**
     * Performs the given action for each entry in this set, possibly
     * reusing one implementation-internal entry instance.
     *
     * <p>This method has the same traversal order and modification
     * semantics as {@link #forEach(java.util.function.Consumer)}, except
     * that the entry passed to {@code action} is a transient view. The
     * entry is valid only for the duration of the current invocation of
     * {@code action}; it must not be retained after {@code action}
     * returns.
     *
     * @implSpec
     * Implementations should traverse the same entries in the same order
     * as {@link #forEach(java.util.function.Consumer)}. Implementations
     * are encouraged to reuse an internal entry instance instead of
     * allocating a new entry for each element. The caller must treat
     * every supplied entry as transient even if an implementation
     * happens to supply distinct instances.
     *
     * @param action the action to be performed for each entry; the entry
     *               passed to the action must not be retained
     * @throws NullPointerException if the specified action is null
     * @throws java.util.ConcurrentModificationException if the map is
     *         structurally modified during traversal (optional behavior)
     *
     * @apiNote
     * Use {@link #forEach(java.util.function.Consumer)} instead if the
     * action needs to retain entries after it returns.
     */
    void viewForEach(Consumer<? super E> action);

    /**
     * Returns an iterator over the entries in this set that may reuse an
     * implementation-internal entry instance.
     *
     * <p>The {@link Iterator#next()} and
     * {@link Iterator#forEachRemaining(java.util.function.Consumer)}
     * methods may return or pass the same entry instance on successive
     * elements. An entry returned by {@code next()} is valid only until
     * the next call to {@code next()}, {@code forEachRemaining()}, or
     * {@code remove()} on the returned iterator. An entry passed to the
     * action of {@code forEachRemaining()} is valid only during that
     * action invocation.
     *
     * @implSpec
     * The returned iterator must traverse the same entries in the same
     * order as {@link #iterator()}. Implementations should reuse an
     * internal entry instance and may allocate a new entry only when
     * reuse is not possible. The caller must nevertheless treat every
     * returned entry as transient.
     *
     * @return an iterator whose entries are transient views; use
     *         {@link #iterator()} if entries must be retained
     *
     * @apiNote
     * Do not store an entry returned by this iterator. To retain an
     * entry, copy its key and value or use {@link #iterator()}.
     */
    Iterator<E> viewIterator();

    /**
     * Returns a spliterator over the entries in this set that may pass an
     * implementation-internal, reused entry to the action.
     *
     * <p>Each entry passed to
     * {@link Spliterator#tryAdvance(java.util.function.Consumer)} or
     * {@link Spliterator#forEachRemaining(java.util.function.Consumer)}
     * is valid only during that action invocation.
     *
     * @implSpec
     * The returned spliterator must traverse the same entries in the same
     * order as {@link #spliterator()}. Implementations are encouraged to
     * reuse an internal entry instance and may return a spliterator whose
     * {@link Spliterator#trySplit()} returns {@code null}. The caller
     * must treat all supplied entries as transient.
     *
     * @return a spliterator whose entries are transient views; use
     *         {@link #spliterator()} if entries must be retained or
     *         parallel traversal is required
     *
     * @apiNote
     * Do not store an entry returned by this spliterator. To retain an
     * entry, copy its key and value or use {@link #spliterator()}.
     */
    Spliterator<E> viewSpliterator();
}
