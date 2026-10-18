package org.boxutil.base.api.container.longtype;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.shorttype.ShortCollection;
import org.boxutil.util.function.longtype.LongShortBiConsumer;
import org.boxutil.util.function.longtype.LongShortBiFunction;
import org.boxutil.util.function.longtype.LongShortBiOperator;
import org.boxutil.util.function.shorttype.ShortShortBiFunction;
import org.boxutil.util.function.shorttype.ShortUnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface LongShortMap extends MapExt<Long, Short> {
    short get(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Long) key).longValue() : (long) 0);
    }

    short getOrDefault(long key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Long)) return defaultValue;
        return this.getOrDefault(key != null ? ((Long) key).longValue() : (long) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Short put(Long key, Short value) {
        return this.put(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short putIfAbsent(Long key, Short value) {
        return this.putIfAbsent(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Long)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0);
    }

    boolean remove(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Long) key).longValue() : (long) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(long key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short replace(Long key, Short value) {
        return this.replace(key != null ? key.longValue() : (long) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(long key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Long key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.longValue() : (long) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(long key, @NotNull LongFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.longValue() : (long) 0, (long l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(long key, @NotNull LongShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.longValue() : (long) 0, (long l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(long key, @NotNull LongShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short compute(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.longValue() : (long) 0, (long l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(long key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short merge(Long key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.longValue() : (long) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(long key, @NotNull ShortUnaryOperator updateFunction);

    @Nullable
    default Short update(Long key, @NotNull UnaryOperator<Short> updateFunction) {
        return this.update(key, (short l_t) -> updateFunction.apply(l_t));
    }

    short remapping(long key, @NotNull LongShortBiFunction<? extends Long> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remapping(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Long> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.longValue() : (long) 0, (long l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull LongShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Short> action) {
        Objects.requireNonNull(action);
        this.forEach((long l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull LongShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Short, ? extends Short> function) {
        Objects.requireNonNull(function);
        this.replaceAll((long l_t, short l_u) -> function.apply(l_t, l_u));
    }

    short getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    short setDefaultReturnValue(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short setDefaultReturnValue(Short newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull LongSet keySet();

    @NotNull ShortCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Long, Short>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Long, Short> {
        long getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Long getKey() {
            return this.getRawKey();
        }

        short getRawValue();

        short setValue(short newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short getValue() {
            return this.getRawValue();
        }

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short setValue(Short value) {
            return this.setValue(value != null ? value.shortValue() : (short) 0);
        }
    }
}
