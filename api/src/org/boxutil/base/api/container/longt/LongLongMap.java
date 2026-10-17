package org.boxutil.base.api.container.longt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.longt.LongLongBiConsumer;
import org.boxutil.util.function.longt.LongLongBiFunction;
import org.boxutil.util.function.longt.LongLongBiOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface LongLongMap extends MapExt<Long, Long> {
    long get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    long getOrDefault(long key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Long put(Long key, Long value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long putIfAbsent(Long key, Long value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long replace(Long key, Long value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(long key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Long key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(long key, @NotNull LongFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (long l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(long key, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(long key, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long compute(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(long key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long merge(Long key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.longValue() : (long) 0, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(long key, @NotNull LongUnaryOperator updateFunction);

    @Nullable
    default Long update(Long key, @NotNull UnaryOperator<Long> updateFunction) {
        return this.update(key, (long l_t) -> updateFunction.apply(l_t));
    }

    long remapping(long key, @NotNull LongLongBiFunction<? extends Long> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remapping(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(@NotNull LongLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach((long l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Long, ? extends Long> function) {
        Objects.requireNonNull(function);
        this.replaceAll((long l_t, long l_u) -> function.apply(l_t, l_u));
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

    @NotNull LongSet keySet();

    @NotNull LongCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Long, Long>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Long, Long> {
        long getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long getKey() {
            return this.getRawKey();
        }

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
