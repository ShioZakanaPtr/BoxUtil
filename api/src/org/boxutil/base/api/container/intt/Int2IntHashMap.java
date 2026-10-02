package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.HashContainer;
import org.boxutil.util.function.IntIntBiConsumer;
import org.boxutil.util.function.IntIntBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;

public interface Int2IntHashMap extends Map<Integer, Integer>, HashContainer<Integer> {
    /**
     * Returns the default return value used when a query does not find a mapping
     * in this container. By default, this value is {@code 0}, but implementations
     * may allow customization via {@link #setDefaultReturnValue(int)}.
     *
     * @return the default return value
     */
    int getDefaultReturnValue();

    /**
     * Sets the default return value used when a query does not find a mapping in
     * this container. This value will be returned by query methods such
     * as {@code get} when the key is absent.
     *
     * @param newDefault the new default return value
     * @return the previous default return value
     */
    int setDefaultReturnValue(int newDefault);

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

    boolean containsValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value == null) return this.containsValue(0);
        if (!(value instanceof Number number)) return false;
        return this.containsValue(number.intValue());
    }

    int get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer get(Object key) {
        if (key == null) return this.get(0);
        if (!(key instanceof Number number)) return this.getDefaultReturnValue();
        return this.get(number.intValue());
    }

    int put(int key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Integer put(Integer key, Integer value) {
        return this.put(key == null ? 0 : key, value == null ? 0 : value);
    }

    int remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key == null) return this.remove(0);
        if (!(key instanceof Number number)) return this.getDefaultReturnValue();
        return this.remove(number.intValue());
    }

    @NotNull IntHashSet keySet();

    @NotNull IntCollection values();

    @NotNull Set<Int2IntHashMap.Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull Set<Map.Entry<Integer, Integer>> entrySet() {
        return (Set) this.rawEntrySet();
    }

    default int getOrDefault(int key, int defaultValue) {
        int value;
        return (((value = this.get(key)) != this.getDefaultReturnValue()) || this.containsKey(key))
                ? value
                : defaultValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        int def = defaultValue == null ? 0 : defaultValue;
        if (key == null) return this.getOrDefault(0, def);
        if (!(key instanceof Number number)) return def;
        return this.getOrDefault(number.intValue(), def);
    }

    default void forEach(IntIntBiConsumer action) {
        Objects.requireNonNull(action);
        for (Int2IntHashMap.Entry entry : this.rawEntrySet()) {
            int k, v;
            try {
                k = entry.getRawKey();
                v = entry.getRawValue();
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
    default void forEach(BiConsumer<? super Integer, ? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEach(action instanceof IntIntBiConsumer cast ? cast : action::accept);
    }

    default void replaceAll(IntBinaryOperator function) {
        Objects.requireNonNull(function);
        for (Int2IntHashMap.Entry entry : this.rawEntrySet()) {
            int k, v;
            try {
                k = entry.getRawKey();
                v = entry.getRawValue();
            } catch (IllegalStateException ise) {
                throw new ConcurrentModificationException(ise);
            }

            v = function.applyAsInt(k, v);

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
    default void replaceAll(BiFunction<? super Integer, ? super Integer, ? extends Integer> function) {
        Objects.requireNonNull(function);
        this.replaceAll(function instanceof IntBinaryOperator cast ? cast : function::apply);
    }


    default int putIfAbsent(int key, int value) {
        int currValue = this.get(key);
        if (currValue == this.getDefaultReturnValue()) {
            currValue = this.put(key, value);
        }
        return currValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer putIfAbsent(Integer key, Integer value) {
        return this.putIfAbsent(key == null ? 0 : key, value == null ? 0 : value);
    }

    default boolean remove(int key, int value) {
        int currValue = this.get(key);
        if (currValue != value || (currValue == this.getDefaultReturnValue() && !this.containsKey(key))) {
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
        if (!(key instanceof Number numberKey)) return false;
        if (!(value instanceof Number numberValue)) return false;
        return this.remove(numberKey.intValue(), numberValue.intValue());
    }

    default boolean replace(int key, int oldValue, int newValue) {
        int currValue = this.get(key);
        if (currValue != oldValue || (currValue == this.getDefaultReturnValue() && !this.containsKey(key))) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, Integer oldValue, Integer newValue) {
        return this.replace(key == null ? 0 : key, oldValue == null ? 0 : oldValue, newValue == null ? 0 : newValue);
    }

    default int replace(int key, int value) {
        int currValue;
        if (((currValue = this.get(key)) != this.getDefaultReturnValue()) || this.containsKey(key)) {
            currValue = this.put(key, value);
        }
        return currValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer replace(Integer key, Integer value) {
        return this.replace(key == null ? 0 : key, value == null ? 0 : value);
    }

    default int computeIfAbsent(int key, IntFunction<? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);

        int oldValue;
        if ((oldValue = this.get(key)) == this.getDefaultReturnValue()) {
            Integer newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                int rawValue = newValue;
                this.put(key, rawValue);
                return rawValue;
            }
        }
        return oldValue;
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer computeIfAbsent(Integer key, Function<? super Integer, ? extends Integer> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key, (int l_key) -> mappingFunction.apply(l_key));
    }

    default int computeIfPresent(int key, IntIntBiFunction<? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        int oldValue, def = this.getDefaultReturnValue();
        if ((oldValue = this.get(key)) != def) {
            Integer newValue = remappingFunction.apply(key, oldValue);
            if (newValue != null) {
                int rawValue = newValue;
                this.put(key, rawValue);
                return rawValue;
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
    default Integer computeIfPresent(Integer key, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    default int compute(int key, IntIntBiFunction<? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        int oldValue = this.get(key), def = this.getDefaultReturnValue();
        Integer newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            if (oldValue != def || this.containsKey(key)) {
                this.remove(key);
            }
            return def;
        } else {
            int rawValue = newValue;
            this.put(key, rawValue);
            return rawValue;
        }
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Integer compute(Integer key, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    default int merge(int key, int value, IntIntBiFunction<? extends Integer> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        if (!this.containsKey(key)) {
            this.put(key, value);
            return value;
        }

        int oldValue = this.get(key);
        Integer newValue = remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            this.remove(key);
            return this.getDefaultReturnValue();
        } else {
            int rawValue = newValue;
            this.put(key, rawValue);
            return rawValue;
        }
    }

    default Integer merge(Integer key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key, value, (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
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
     * function, unless the existing value is equal to the default return value, in
     * which case the current value is used.
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
     * int def = getDefaultReturnValue();
     * if (!containsKey(key)) return def;
     *
     * int currValue = get(key);
     * Integer newKey = remappingFunction.apply(key, currValue);
     *
     * if (newKey == null || key != newKey)
     *     remove(key);
     * if (newKey != null && key != newKey) {
     *     if (containsKey(newKey)) {
     *         int oldValue = get(newKey);
     *         if (oldValue != def) {
     *             Integer newValue = mergeFunction.apply(oldValue, currValue);
     *             if (newValue != null)
     *                 currValue = newValue;
     *             else
     *                 return def;
     *         }
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
     * @throws IllegalArgumentException if some property of the specified key
     *         or value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if {@code remappingFunction} or
     *         {@code mergeFunction} is {@code null}
     */
    default int remapping(int key, IntIntBiFunction<? extends Integer> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);

        int def = this.getDefaultReturnValue();
        if (!this.containsKey(key)) return def;

        int currValue = this.get(key);
        Integer newKey = remappingFunction.apply(key, currValue);

        if (newKey == null || key != newKey) this.remove(key);
        if (newKey != null && key != newKey) {
            if (this.containsKey(newKey.intValue())) {
                int oldValue = this.get(newKey);
                if (oldValue != def) {
                    Integer newValue = mergeFunction.apply(oldValue, currValue);
                    if (newValue != null) currValue = newValue;
                    else return def;
                }
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
    void swap(Map<Integer, Integer> source);

    interface Entry extends Map.Entry<Integer, Integer> {
        int getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getKey() {
            return this.getRawKey();
        }

        int getRawValue();

        int setValue(int value);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer setValue(Integer value) {
            return this.setValue(value.intValue());
        }
    }
}
