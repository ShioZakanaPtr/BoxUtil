package org.boxutil.base.api.container.intt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.shortt.ShortCollection;
import org.boxutil.util.function.intt.IntShortBiConsumer;
import org.boxutil.util.function.intt.IntShortBiFunction;
import org.boxutil.util.function.intt.IntShortBiOperator;
import org.boxutil.util.function.shortt.ShortShortBiFunction;
import org.boxutil.util.function.shortt.ShortUnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.*;

public interface IntShortMap extends MapExt<Integer, Short> {
    short get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    short getOrDefault(int key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Short put(Integer key, Short value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short putIfAbsent(Integer key, Short value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short replace(Integer key, Short value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(int key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(Integer key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(int key, @NotNull IntFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (int l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(int key, @NotNull IntShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(int key, @NotNull IntShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short compute(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(int key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short merge(Integer key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key != null ? key.intValue() : (int) 0, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(int key, @NotNull ShortUnaryOperator updateFunction);

    @Nullable
    default Short update(Integer key, @NotNull UnaryOperator<Short> updateFunction) {
        return this.update(key, (short l_t) -> updateFunction.apply(l_t));
    }

    short remapping(int key, @NotNull IntShortBiFunction<? extends Integer> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull IntShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        Objects.requireNonNull(action);
        this.forEach((int l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Short, ? extends Short> function) {
        Objects.requireNonNull(function);
        this.replaceAll((int l_t, short l_u) -> function.apply(l_t, l_u));
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

    @NotNull IntSet keySet();

    @NotNull ShortCollection values();

    @NotNull SetExt<Entry> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<Integer, Short>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry extends Map.Entry<Integer, Short> {
        int getRawKey();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         */
        @Deprecated
        default Integer getKey() {
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
