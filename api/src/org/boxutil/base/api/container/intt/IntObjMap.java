package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.intt.IntObjBiConsumer;
import org.boxutil.util.function.intt.IntObjBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface IntObjMap<V> extends MapExt<Integer, V> {
    V get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    V getOrDefault(int key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue);
    }

    V put(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable V put(Integer key, V value) {
        return this.put(key != null ? key.intValue() : (int) 0, value);
    }

    V putIfAbsent(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V putIfAbsent(Integer key, V value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value);
    }

    V remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value);
    }

    V replace(int key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V replace(Integer key, V value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value);
    }

    boolean replace(int key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, V oldValue, V newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue, newValue);
    }

    V computeIfAbsent(int key, @NotNull IntFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (int l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(int key, @NotNull IntObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(int key, @NotNull IntObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V compute(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(int key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V merge(Integer key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.intValue() : (int) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(int key, @NotNull UnaryOperator<V> updateFunction);

    @Nullable
    default V update(Integer key, @NotNull UnaryOperator<V> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, updateFunction);
    }

    V remapping(int key, IntObjBiFunction<? super V, ? extends Integer> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remapping(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends Integer> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.containsKey(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    void forEach(IntObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super Integer, ? super V> action) {
        Objects.requireNonNull(action);
        this.forEach((int l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(IntObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super Integer, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        this.replaceAll((int l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull IntSet keySet();

    @NotNull SetExt<Entry<V>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Integer, V>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<V> extends Map.Entry<Integer, V> {
        int getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getKey() {
            return this.getRawKey();
        }
    }
}
