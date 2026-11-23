package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;

import java.util.ConcurrentModificationException;
import java.util.function.BiConsumer;

/**
 * A map that has a well-defined encounter order and supports operations at both
 * ends.
 *
 * <p>This interface exists to provide a uniform, forward-compatible view of the
 * first/last entry operations introduced in JDK 21 as part of the
 * {@code SequencedMap} hierarchy. Developers targeting older JDK versions can
 * program against this interface to ease future migration, while developers on
 * newer JDK versions are ensured that calls resolve to the correct implementation
 * for the underlying map type via method signature.
 *
 * <p>When a map implements this interface, callers should consult the
 * corresponding method documentation in JDK 21's
 * <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html">
 * {@code SequencedMap}</a> interface for the full contract, including
 * encounter-order semantics and optional-operation behavior.
 *
 * <p><strong>Note:</strong> This interface does not adapt the
 * {@code reversed()} method of {@code SequencedMap}. It only provides a
 * forward-compatible view of the first/last-entry operations. Callers requiring
 * a reversed view should use the underlying map's JDK 21 {@code SequencedMap}
 * API directly, when available.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @see MapExt
 * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html">
 *      SequencedMap (JDK 21)</a>
 *
 * @since 1.0.0
 */
public interface OrderedMap<K, V> extends MapExt<K, V> {
    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html#firstEntry()">
     *      SequencedMap.firstEntry()</a>
     */
    MapExt.Entry<K, V> firstEntry();

    /**
     * Applies the given action directly to the first key-value pair in this
     * map's encounter order, without constructing an intermediate
     * {@link MapExt.Entry}.
     *
     * <p>If this map is empty, the action is not invoked.
     *
     * @param action the action to perform on the first key-value pair
     * @return {@code true} if the operation succeeded, i.e. this map contains
     *         at least one element; {@code false} if this map is empty
     * @throws NullPointerException if {@code action} is {@code null}
     */
    boolean forFirst(@NotNull BiConsumer<? super K, ? super V> action);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html#firstEntry()">
     *      SequencedMap.lastEntry()</a>
     */
    MapExt.Entry<K, V> lastEntry();

    /**
     * Applies the given action directly to the last key-value pair in this
     * map's encounter order, without constructing an intermediate
     * {@link MapExt.Entry}.
     *
     * <p>If this map is empty, the action is not invoked.
     *
     * @param action the action to perform on the last key-value pair
     * @return {@code true} if the operation succeeded, i.e. this map contains
     *         at least one element; {@code false} if this map is empty
     * @throws NullPointerException if {@code action} is {@code null}
     */
    boolean forLast(@NotNull BiConsumer<? super K, ? super V> action);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html#putFirst(K,V)">
     *      SequencedMap.putFirst(K, V)</a>
     */
    V putFirst(K key, V value);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html#putLast(K,V)">
     *      SequencedMap.putLast(K, V)</a>
     */
    V putLast(K key, V value);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html#pollFirstEntry()">
     *      SequencedMap.pollFirstEntry()</a>
     */
    MapExt.Entry<K, V> pollFirstEntry();

    /**
     * Removes and returns the first key-value pair in this map's encounter
     * order, then, if the removal succeeded, passes the removed key and value
     * directly to the given action without constructing an intermediate
     * {@link MapExt.Entry}.
     *
     * <p>If this map is empty, no entry is removed and the action is not
     * invoked. Otherwise, the first key-value pair is safely removed first,
     * and the action is invoked with that removed pair only after the removal
     * succeeds.
     *
     * @param action the action to perform on the removed first key-value pair
     * @return {@code true} if an entry was successfully removed (i.e. this map
     *         was not empty); {@code false} if this map is empty and no entry
     *         was removed
     * @throws NullPointerException if {@code action} is {@code null}
     */
    boolean pollFirst(@NotNull BiConsumer<? super K, ? super V> action);

    /**
     * @see <a href="https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/SequencedMap.html#pollLastEntry()">
     *      SequencedMap.pollLastEntry()</a>
     */
    MapExt.Entry<K, V> pollLastEntry();

    /**
     * Removes and returns the last key-value pair in this map's encounter
     * order, then, if the removal succeeded, passes the removed key and value
     * directly to the given action without constructing an intermediate
     * {@link MapExt.Entry}.
     *
     * <p>If this map is empty, no entry is removed and the action is not
     * invoked. Otherwise, the last key-value pair is safely removed first,
     * and the action is invoked with that removed pair only after the removal
     * succeeds.
     *
     * @param action the action to perform on the removed last key-value pair
     * @return {@code true} if an entry was successfully removed (i.e. this map
     *         was not empty); {@code false} if this map is empty and no entry
     *         was removed
     * @throws NullPointerException if {@code action} is {@code null}
     */
    boolean pollLast(@NotNull BiConsumer<? super K, ? super V> action);

    /**
     * Performs the given action for each key-value pair in this map in reverse
     * encounter order, passing the key and value directly to the action without
     * constructing an intermediate {@code Map.Entry}.
     *
     * <p>Traversal starts with the last key-value pair in this map's encounter
     * order and proceeds toward the first, i.e. in the reverse of the order
     * used when traversing from first to last. If this map is empty, the action
     * is not invoked.
     *
     * <p>This method performs a reverse traversal only; it does not return or
     * adapt a reversed view of this map.
     *
     * @param action the action to perform on each key-value pair
     * @throws NullPointerException if {@code action} is {@code null}
     * @throws ConcurrentModificationException if a key-value pair is found to be
     *         removed during iteration
     */
    void forEachReversed(@NotNull BiConsumer<? super K, ? super V> action);

    /**
     * {@inheritDoc}
     *
     * @see OrderedSet
     */
    @NotNull
    OrderedSet<K> keySet();

    /**
     * {@inheritDoc}
     *
     * @see OrderedCollection
     */
    @NotNull
    OrderedCollection<V> values();

    /**
     * {@inheritDoc}
     *
     * @see OrderedEntrySet
     */
    @NotNull
    OrderedEntrySet<MapExt.Entry<K, V>> entrySet();
}
