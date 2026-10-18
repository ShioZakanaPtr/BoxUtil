package org.boxutil.base.api.container;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

/**
 * For any key or value is {@code null}, it should be treated as {@code 0}
 * if the key was specific primitive type that the Map implementation defined.
 * <p>For any value is {@code 0}, it also be treated as {@code null} if the
 * value was specific primitive type that the Map implementation defined.
 */
public interface MapExt<K, V> extends Map<K, V>, Container {
    /**
     * Returns the default return value used when a query does not find a mapping
     * in this container. By default, this value is {@code null} or {@code 0}, but
     * implementations may allow customization via {@link #setDefaultReturnValue(V)}.
     *
     * @return the default return value
     */
    default V getDefaultReturnValue() {
        return null;
    }

    /**
     * Sets the default return value used when a query does not find a mapping in
     * this container. This value will be returned by query methods such
     * as {@code get} when the key is absent.
     *
     * @param newDefault the new default return value
     * @return the previous default return value
     * @throws UnsupportedOperationException if this map using a fixed default
     *         return value.
     */
    default V setDefaultReturnValue(@Nullable V newDefault) {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates the mapping for the specified key using the given update function.
     * If the specified key is not already associated with a value, the update
     * function is applied to {@code null}; otherwise, it is applied to the
     * current value. The resulting value is then associated with the specified
     * key, regardless of whether it is {@code null}.
     *
     * <p>If the update function returns {@code null}, the mapping for the
     * specified key is set to {@code null} (if this map permits null values);
     * the mapping is not removed. This differs from
     * {@link Map#compute(Object, java.util.function.BiFunction)}, which removes
     * the mapping when the remapping function returns {@code null}.
     *
     * <p>If the specified key is not present, and this map can also return a
     * default return value, the update function receives that value instead
     * of {@code null}.
     *
     * <p>If the update function itself throws an (unchecked) exception, the
     * exception is rethrown, and the mapping is left unchanged.
     *
     * <p>The update function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following steps
     * for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V oldValue = get(key);
     * V newValue = updateFunction.apply(oldValue);
     * put(key, newValue);
     * return newValue;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key whose mapping is to be updated
     * @param updateFunction the function to compute a new value from the current
     *        value, or from {@code null} if the specified key is not present
     * @return the new value associated with the specified key, which may be
     *         {@code null}
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if {@code updateFunction} is {@code null},
     *         or if the specified key is {@code null} and this map does not
     *         permit null keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>),
     *         or if the new value is {@code null} and this map does not permit
     *         null values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default @Nullable V update(K key, @NotNull UnaryOperator<V> updateFunction) {
        Objects.requireNonNull(updateFunction);

        V oldValue = this.get(key);
        // if the `get` method also can return a default value, directly use it
        V newValue = updateFunction.apply(oldValue);
        this.put(key, newValue);
        return newValue;
    }

    /**
     * Remaps the mapping for the specified key. If the specified key is not
     * already associated with a value, returns the default return value.
     * Otherwise, computes a new key from the specified key and its current value
     * using the given remapping function, and associates the current value with
     * the new key, merging with any existing value at the new key using the given
     * merge function when necessary.
     *
     * <p>If the remapping function returns {@code null}, the mapping for the
     * specified key is removed and the default return value is returned. If the
     * remapping function returns the same key, the mapping is left unchanged. If
     * the remapping function returns a different non-{@code null} key, the mapping
     * for the specified key is removed and the current value is associated with the
     * new key. If the new key already has a value, the existing value and the
     * current value are combined using the merge function, even if the existing
     * value is {@code null}.
     *
     * <p>If the merge function returns {@code null}, the mapping for the new key
     * (if any) is removed and the default return value is returned. If either
     * function itself throws an (unchecked) exception, the exception is rethrown.
     *
     * <p>The remapping and merge functions should not modify this map during
     * computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following steps
     * for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V def = getDefaultReturnValue();
     * if (!containsKey(key)) return def;
     *
     * V currValue = get(key);
     * K newKey = remappingFunction.apply(key, currValue);
     *
     * if (newKey == null || !Objects.equals(newKey, key))
     *     remove(key);
     * if (newKey == null)
     *     return def;
     * if (!Objects.equals(newKey, key)) {
     *     if (containsKey(newKey)) {
     *         V oldValue = get(newKey);
     *         V newValue = mergeFunction.apply(oldValue, currValue);
     *         if (newValue != null) {
     *             currValue = newValue;
     *         } else {
     *             remove(newKey);
     *             return def;
     *         }
     *     }
     *     put(newKey, currValue);
     * }
     * return currValue;
     * }</pre>
     *
     * <p>The default implementation makes no guarantees about synchronization
     * or atomicity properties of this method. Any implementation providing
     * atomicity guarantees must override this method and document its
     * concurrency properties.
     *
     * @param key key whose mapping is to be remapped
     * @param remappingFunction the function to compute a new key from the
     *        specified key and its current value
     * @param mergeFunction the function to merge an existing value at the new key
     *        with the current value
     * @return the resulting value for the specified key, or the default return
     *         value if the specified key is not present, the remapping function
     *         returns {@code null}, or the merge function returns {@code null}
     * @throws UnsupportedOperationException if the {@code put} or {@code remove}
     *         operation is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if {@code remappingFunction} or
     *         {@code mergeFunction} is {@code null}
     */
    default V remapping(K key, @NotNull BiFunction<? super K, ? super V, ? extends K> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);

        V def = this.getDefaultReturnValue();
        if (!this.containsKey(key)) return def;

        V currValue = this.get(key);
        K newKey = remappingFunction.apply(key, currValue);

        if (newKey == null || !Objects.equals(newKey, key)) this.remove(key);
        if (newKey == null) return def;
        if (!Objects.equals(newKey, key)) {
            if (this.containsKey(newKey)) {
                V oldValue = this.get(newKey),
                        // if the `get` method also can return a default value, should check it here and use null for result, because it means "not found"
                        newValue = mergeFunction.apply(oldValue == def ? null : oldValue, currValue);
                if (newValue != null) {
                    currValue = newValue;
                } else {
                    this.remove(newKey);
                    return def;
                }
            }
            this.put(newKey, currValue);
        }
        return currValue;
    }

    /**
     * Swaps the mappings between this map and the specified map. After this
     * operation, this map contains the mappings previously held by the
     * specified map, and the specified map contains the mappings previously
     * held by this map. If the specified map is the same as this map, the
     * operation has no effect.
     *
     * <p>The specified map must be compatible with this map such that all
     * mappings can be exchanged without altering their keys or values.
     *
     * <p>Unless {@code source} is this map, this operation is a structural
     * modification. Iterators, spliterators, and other traversal objects
     * previously obtained from either map are considered invalid after this
     * operation; their subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}. For maps, previously
     * obtained {@code keySet}, {@code values}, and {@code entrySet} views may
     * remain associated with the map object, but their iterators and contents
     * after the swap are implementation-specific.
     *
     * <p><b>Implementation Note:</b> Implementations may perform the swap by
     * exchanging internal data structures directly instead of transferring
     * mappings one by one. In that case, references to internal data obtained
     * through implementation-specific means (if any) may observe the contents
     * now held by the other map, and previously obtained views or iterators
     * may be invalidated. This interface does not specify the identity,
     * validity, or fail-fast behavior of such views, iterators, or internal
     * references after a swap.
     *
     * @implSpec
     * The default implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param source the map whose mappings are to be exchanged with the
     *        mappings of this map
     * @throws NullPointerException if the specified map is {@code null}
     * @throws UnsupportedOperationException if the swap operation is not
     *         supported by this map or the specified map; an implementation
     *         that performs the swap using basic mutation operations may also
     *         throw if those operations are unsupported
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of a key or value in the
     *         specified map prevents it from being stored in this map, or
     *         vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a key or value in
     *         the specified map prevents it from being stored in this map, or
     *         vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    default void swap(@NotNull Map<K, V> source) {
        throw new UnsupportedOperationException();
    }

    @NotNull SetExt<K> keySet();

    @NotNull CollectionExt<V> values();

    @NotNull SetExt<Entry<K, V>> entrySet();
}
