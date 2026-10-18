package org.boxutil.base.api.container.objecttype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.longtype.LongCollection;
import org.boxutil.util.function.longtype.LongLongBiFunction;
import org.boxutil.util.function.objecttype.ObjLongBiConsumer;
import org.boxutil.util.function.objecttype.ObjLongBiFunction;
import org.boxutil.util.function.objecttype.ObjLongBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface ObjLongMap<K> extends MapExt<K, Long> {
    long getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long get(Object key) {
        return this.getRaw(key);
    }

    long getOrDefault(Object key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Long put(K key, Long value) {
        return this.put(key, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long putIfAbsent(K key, Long value) {
        return this.putIfAbsent(key, value != null ? value.longValue() : (long) 0);
    }

    long removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, long value);

    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long replace(K key, Long value) {
        return this.replace(key, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(K key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(K key, Long oldValue, Long newValue) {
        return this.replace(key, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfAbsent(K key, @NotNull Function<? super K, ? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(K key, @NotNull ObjLongBiFunction<? super K, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key, (K l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(K key, @NotNull ObjLongBiFunction<? super K, ? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long compute(K key, @NotNull BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key, (K l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(K key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    long update(K key, @NotNull LongUnaryOperator updateFunction);

    @Nullable
    default Long update(K key, @NotNull UnaryOperator<Long> updateFunction) {
        return this.update(key, (long l_t) -> updateFunction.apply(l_t));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long merge(K key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long remapping(K key, @NotNull ObjLongBiFunction<? super K, ? extends K> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remapping(K key, @NotNull BiFunction<? super K, ? super Long, ? extends K> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key, (K l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(ObjLongBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super K, ? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach((K l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(ObjLongBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super K, ? super Long, ? extends Long> function) {
        Objects.requireNonNull(function);
        this.replaceAll((K l_t, long l_u) -> function.apply(l_t, l_u));
    }

    long getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    long setDefaultReturnValue(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long setDefaultReturnValue(Long newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull LongCollection values();

    @NotNull SetExt<ObjLongMap.Entry<K>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<K, Long>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<K> extends Map.Entry<K, Long> {
        long getRawValue();

        long setValue(long newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long setValue(Long value) {
            return this.setValue(value != null ? value.longValue() : (long) 0);
        }
    }
}
