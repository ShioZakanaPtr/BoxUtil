package org.boxutil.base.api.container.longt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.longt.LongObjBiConsumer;
import org.boxutil.util.function.longt.LongObjBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface LongObjMap<V> extends MapExt<Long, V> {
    V get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    V getOrDefault(long key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue);
    }

    V put(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable V put(Long key, V value) {
        return this.put(key != null ? key.longValue() : (long) 0, value);
    }

    V putIfAbsent(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V putIfAbsent(Long key, V value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value);
    }

    V remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value);
    }

    V replace(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V replace(Long key, V value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value);
    }

    boolean replace(long key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Long key, V oldValue, V newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue, newValue);
    }

    V computeIfAbsent(long key, @NotNull LongFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (long l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(long key, @NotNull LongObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(long key, @NotNull LongObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V compute(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(long key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V merge(Long key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.longValue() : (long) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(long key, @NotNull UnaryOperator<V> updateFunction);

    @Nullable
    default V update(Long key, @NotNull UnaryOperator<V> updateFunction) {
        return this.update(key != null ? key.longValue() : (long) 0, updateFunction);
    }

    V remapping(long key, LongObjBiFunction<? super V, ? extends Long> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default V remapping(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends Long> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Long)) return false;
        return this.containsKey(key != null ? ((Long) key).longValue() : (long) 0);
    }

    void forEach(LongObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super Long, ? super V> action) {
        Objects.requireNonNull(action);
        this.forEach((long l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(LongObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super Long, ? super V, ? extends V> function) {
        Objects.requireNonNull(function);
        this.replaceAll((long l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull LongSet keySet();

    @NotNull SetExt<Entry<V>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Long, V>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<V> extends Map.Entry<Long, V> {
        long getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long getKey() {
            return this.getRawKey();
        }
    }
}
