package org.boxutil.base.api.container.shortt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.util.function.shortt.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ShortShortMap extends MapExt<Short, Short> {
    short get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    short getOrDefault(short key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Short put(Short key, Short value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short putIfAbsent(Short key, Short value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short replace(Short key, Short value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(short key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Short key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(short key, @NotNull ShortFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (short l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(short key, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(short key, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short compute(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(short key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short merge(Short key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.shortValue() : (short) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(short key, @NotNull ShortUnaryOperator updateFunction);

    @Nullable
    default Short update(Short key, @NotNull UnaryOperator<Short> updateFunction) {
        return this.update(key, (short l_t) -> updateFunction.apply(l_t));
    }

    short remapping(short key, @NotNull ShortShortBiFunction<? extends Short> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remapping(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsKey(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Short)) return false;
        return this.containsKey(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean containsValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull ShortShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Short> action) {
        Objects.requireNonNull(action);
        this.forEach((short l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Short, ? extends Short> function) {
        Objects.requireNonNull(function);
        this.replaceAll((short l_t, short l_u) -> function.apply(l_t, l_u));
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

    @NotNull ShortSet keySet();

    @NotNull ShortCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Short, Short>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Short, Short> {
        short getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Short getKey() {
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
