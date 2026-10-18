package org.boxutil.base.api.container.inttype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.longtype.LongCollection;
import org.boxutil.util.function.inttype.IntLongBiConsumer;
import org.boxutil.util.function.inttype.IntLongBiFunction;
import org.boxutil.util.function.inttype.IntLongBiOperator;
import org.boxutil.util.function.longtype.LongLongBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface IntLongMap extends MapExt<Integer, Long> {
    long get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    long getOrDefault(int key, long defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.longValue() : (long) 0);
    }

    long put(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Long put(Integer key, Long value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    long putIfAbsent(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long putIfAbsent(Integer key, Long value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    long remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Long) value).longValue() : (long) 0);
    }

    long replace(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long replace(Integer key, Long value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    boolean replace(int key, long oldValue, long newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, Long oldValue, Long newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.longValue() : (long) 0, newValue != null ? newValue.longValue() : (long) 0);
    }

    long computeIfAbsent(int key, @NotNull IntFunction<? extends Long> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Long> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (int l_t) -> mappingFunction.apply(l_t));
    }

    long computeIfPresent(int key, @NotNull IntLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long compute(int key, @NotNull IntLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long compute(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long merge(int key, long value, @NotNull LongLongBiFunction<? extends Long> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long merge(Integer key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.intValue() : (int) 0, value.longValue(), (long l_t, long l_u) -> remappingFunction.apply(l_t, l_u));
    }

    long update(int key, @NotNull LongUnaryOperator updateFunction);

    @Nullable
    default Long update(Integer key, @NotNull UnaryOperator<Long> updateFunction) {
        return this.update(key, (long l_t) -> updateFunction.apply(l_t));
    }

    long remapping(int key, @NotNull IntLongBiFunction<? extends Integer> remappingFunction, LongLongBiFunction<? extends Long> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Long remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, long l_u) -> remappingFunction.apply(l_t, l_u), (long l_t, long l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Long)) return false;
        return this.containsValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    void forEach(@NotNull IntLongBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach((int l_t, long l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntLongBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Long, ? extends Long> function) {
        Objects.requireNonNull(function);
        this.replaceAll((int l_t, long l_u) -> function.apply(l_t, l_u));
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

    @NotNull IntSet keySet();

    @NotNull LongCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Integer, Long>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Integer, Long> {
        int getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getKey() {
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
