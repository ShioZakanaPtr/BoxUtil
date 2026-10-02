package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.HashContainer;
import org.boxutil.util.function.IntXBiConsumer;
import org.boxutil.util.function.IntXBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public interface Int2ObjHashMap<V> extends Map<Integer, V>, HashContainer<Integer> {
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

    boolean containsKey(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key == null) return this.containsKey(0);
        if (!(key instanceof Number number)) return false;
        return this.containsKey(number.intValue());
    }

    V get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V get(Object key) {
        if (key == null) return this.get(0);
        if (!(key instanceof Number number)) return this.getDefaultReturnValue();
        return this.get(number.intValue());
    }

    @Nullable V put(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable V put(Integer key, V value) {
        return this.put(key == null ? 0 : key, value);
    }

    V remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remove(Object key) {
        if (key == null) return this.remove(0);
        if (!(key instanceof Number number)) return this.getDefaultReturnValue();
        return this.remove(number.intValue());
    }

    @NotNull IntHashSet keySet();

    @NotNull Collection<V> values();

    @NotNull Set<Int2ObjHashMap.Entry<V>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull Set<Map.Entry<Integer, V>> entrySet() {
        return (Set) this.rawEntrySet();
    }

    default V getOrDefault(int key, V defaultValue) {
        V value;
        return (((value = this.get(key)) != this.getDefaultReturnValue()) || this.containsKey(key))
                ? value
                : defaultValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key == null) return this.getOrDefault(0, defaultValue);
        if (!(key instanceof Number number)) return defaultValue;
        return this.getOrDefault(number.intValue(), defaultValue);
    }

    default void forEach(IntXBiConsumer<? super V> action) {
        Objects.requireNonNull(action);
        for (Int2ObjHashMap.Entry<V> entry : this.rawEntrySet()) {
            int k;
            V v;
            try {
                k = entry.getRawKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super Integer, ? super V> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }

    default void replaceAll(IntXBiFunction<? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        for (Int2ObjHashMap.Entry<V> entry : this.rawEntrySet()) {
            int k;
            V v;
            try {
                k = entry.getRawKey();
                v = entry.getValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }

            v = function.apply(k, v);

            try {
                entry.setValue(v);
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        this.replaceAll(function::apply);
    }

    @Nullable
    default V putIfAbsent(int key, V value) {
        V currValue = this.get(key);
        if (currValue == null || currValue == this.getDefaultReturnValue()) {
            currValue = this.put(key, value);
        }
        return currValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @Nullable
    default V putIfAbsent(Integer key, V value) {
        return this.putIfAbsent(key == null ? 0 : key, value);
    }

    default boolean remove(int key, Object value) {
        Object currValue = this.get(key);
        if (!Objects.equals(currValue, value) ||
                (currValue == this.getDefaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.remove(key);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return this.remove((int) (key == null ? 0 : key), value);
    }

    default boolean replace(int key, V oldValue, V newValue) {
        Object currValue = this.get(key);
        if (!Objects.equals(currValue, oldValue) ||
                (currValue == this.getDefaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, V oldValue, V newValue) {
        return this.replace(key == null ? 0 : key, oldValue, newValue);
    }

    @Nullable
    default V replace(int key, V value) {
        V currValue;
        if (((currValue = this.get(key)) != this.getDefaultReturnValue()) || this.containsKey(key)) {
            currValue = this.put(key, value);
        }
        return currValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    @Nullable
    default V replace(Integer key, V value) {
        return this.replace(key == null ? 0 : key, value);
    }

    default V computeIfAbsent(int key, IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);

        V oldValue;
        if ((oldValue = this.get(key)) == null || oldValue == this.getDefaultReturnValue()) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                this.put(key, newValue);
                return newValue;
            }
        }
        return oldValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfAbsent(Integer key, Function<? super Integer, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key, (int l_key) -> mappingFunction.apply(l_key));
    }

    default V computeIfPresent(int key, IntXBiFunction<? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        V oldValue, def = this.getDefaultReturnValue();
        if ((oldValue = this.get(key)) != null && oldValue != def) {
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                this.put(key, newValue);
                return newValue;
            } else {
                this.remove(key);
                return def;
            }
        } else {
            return def;
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfPresent(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    default V compute(int key, IntXBiFunction<? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        V oldValue = get(key),
                newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            if (oldValue != null || containsKey(key)) {
                remove(key);
            }
            return this.getDefaultReturnValue();
        } else {
            put(key, newValue);
            return newValue;
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V compute(Integer key, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    default V merge(int key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);

        V oldValue = get(key), def = this.getDefaultReturnValue(),
                newValue = (oldValue == null || oldValue == def) ? value : remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            remove(key);
            return def;
        } else {
            put(key, newValue);
            return newValue;
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V merge(Integer key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);
        return this.merge(key == null ? 0 : key, value, remappingFunction);
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
     * Integer newKey = remappingFunction.apply(key, currValue);
     *
     * if (newKey == null || key != newKey)
     *     remove(key);
     * if (newKey != null && key != newKey) {
     *     if (containsKey(newKey)) {
     *         V oldValue = get(newKey);
     *         V newValue = (oldValue == null || oldValue == def) ? currValue
     *                 : mergeFunction.apply(oldValue, currValue);
     *         if (newValue != null)
     *             currValue = newValue;
     *         else
     *             return def;
     *     }
     *     put(newKey.intValue(), currValue);
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
    default V remapping(int key, IntXBiFunction<? super V, ? extends Integer> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);

        V def = this.getDefaultReturnValue();
        if (!this.containsKey(key)) return def;

        V currValue = this.get(key);
        Integer newKey = remappingFunction.apply(key, currValue);

        if (newKey == null || key != newKey) this.remove(key);
        if (newKey != null && key != newKey) {
            if (this.containsKey(newKey.intValue())) {
                V oldValue = this.get(newKey),
                        newValue = (oldValue == null || oldValue == def) ? currValue : mergeFunction.apply(oldValue, currValue);
                if (newValue != null) currValue = newValue;
                else return def;
            }
            this.put(newKey.intValue(), currValue);
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
    void swap(Map<Integer, V> source);

    interface Entry<V> extends Map.Entry<Integer, V> {
        int getRawKey();

        @Deprecated
        default Integer getKey() {
            return this.getRawKey();
        }
    }
}
