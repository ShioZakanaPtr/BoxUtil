package org.boxutil.base.api.container.objectt;

import org.boxutil.base.api.container.ContainerBehavior;
import org.boxutil.base.api.container.HashContainer;
import org.boxutil.base.api.container.intt.IntHashSet;
import org.boxutil.util.function.IntXBiConsumer;
import org.boxutil.util.function.IntXBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface Obj2ObjHashMap<K, V> extends Map<K, V>, HashContainer<K> {
    /**
     * Returns the default return value used when a query does not find a mapping
     * in this container. By default, this value is {@code null}, but implementations
     * may allow customization via {@link #setDefaultReturnValue(V)}.
     *
     * @return the default return value
     */
    V getDefaultReturnValue();

    /**
     * Sets the default return value used when a query does not find a mapping in
     * this container. This value will be returned by query methods such
     * as {@code get} when the key is absent.
     *
     * @param newDefault the new default return value
     * @return the previous default return value
     */
    V setDefaultReturnValue(V newDefault);

    /**
     * Remaps the mapping for the specified key. If the specified key is not
     * already associated with a value, returns the default return value.
     * Otherwise, computes a new key from the specified key and its current value
     * using the given remapping function, and associates the current value with
     * the new key, merging with any existing value at the new key using the given
     * merge function when necessary.
     *
     * <p>If the remapping function returns {@code null}, the mapping for the
     * specified key is removed. If the remapping function returns the same key,
     * the mapping is left unchanged. If the remapping function returns a different
     * non-{@code null} key, the mapping for the specified key is removed and the
     * current value is associated with the new key. If the new key already has a
     * value, the existing value and the current value are combined using the merge
     * function, unless the existing value is {@code null} or is the default return
     * value, in which case the current value is used.
     *
     * <p>If the merge function returns {@code null}, the default return value is
     * returned and no mapping for the new key is created or updated. If either
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
     * ContainerBehavior<K> behavior = this.getBehavior();
     * if (newKey == null || !behavior.equals(key, newKey))
     *     remove(key);
     * if (newKey != null && !behavior.equals(key, newKey)) {
     *     if (containsKey(newKey)) {
     *         V oldValue = get(newKey);
     *         V newValue = (oldValue == null || oldValue == def) ? currValue
     *                 : mergeFunction.apply(oldValue, currValue);
     *         if (newValue != null)
     *             currValue = newValue;
     *         else
     *             return def;
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
     *         value if the specified key is not present or the merge function
     *         returns {@code null}
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
    default V remapping(K key, BiFunction<? super K, ? super V, ? extends K> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);

        V def = this.getDefaultReturnValue();
        if (!this.containsKey(key)) return def;

        V currValue = this.get(key);
        K newKey = remappingFunction.apply(key, currValue);

        ContainerBehavior<K> behavior = this.getBehavior();
        if (newKey == null || !behavior.equals(key, newKey)) this.remove(key);
        if (newKey != null && !behavior.equals(key, newKey)) {
            if (this.containsKey(newKey)) {
                V oldValue = this.get(newKey),
                        newValue = (oldValue == null || oldValue == def) ? currValue : mergeFunction.apply(oldValue, currValue);
                if (newValue != null) currValue = newValue;
                else return def;
            }
            this.put(newKey, currValue);
        }
        return currValue;
    }

    /**
     * Swaps the mappings between this map and the specified map. After this
     * operation, this map contains the mappings previously held by the specified
     * map, and the specified map contains the mappings previously held by this
     * map. If the specified map is the same as this map, the operation has no
     * effect.
     *
     * <p>The specified map must be compatible with this map such that all
     * mappings can be exchanged without altering their keys or values.
     *
     * @param source the map whose mappings are to be exchanged with the mappings
     *        of this map
     * @throws NullPointerException if the specified map is null
     * @throws UnsupportedOperationException if the {@code put}, {@code remove},
     *         or {@code clear} operation is not supported by this map or the
     *         specified map
     * @throws ClassCastException if the class of a key or value in the specified
     *         map prevents it from being stored in this map, or vice versa
     * @throws IllegalArgumentException if some property of a key or value in the
     *         specified map prevents it from being stored in this map, or vice
     *         versa
     */
    void swap(Map<K, V> source);
}
