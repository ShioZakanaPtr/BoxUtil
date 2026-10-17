package org.boxutil.base.api.container.objectt;

import org.boxutil.base.api.container.MapExt;
import org.boxutil.base.api.container.SetExt;
import org.boxutil.base.api.container.shortt.ShortCollection;
import org.boxutil.util.function.objectt.ObjShortBiConsumer;
import org.boxutil.util.function.objectt.ObjShortBiFunction;
import org.boxutil.util.function.objectt.ObjShortBiOperator;
import org.boxutil.util.function.shortt.ShortShortBiFunction;
import org.boxutil.util.function.shortt.ShortUnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ObjShortMap<K> extends MapExt<K, Short> {
    short getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short get(Object key) {
        return this.getRaw(key);
    }

    short getOrDefault(Object key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @Nullable Short put(K key, Short value) {
        return this.put(key, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short putIfAbsent(K key, Short value) {
        return this.putIfAbsent(key, value != null ? value.shortValue() : (short) 0);
    }

    short removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, short value);

    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short replace(K key, Short value) {
        return this.replace(key, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(K key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default boolean replace(K key, Short oldValue, Short newValue) {
        return this.replace(key, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfAbsent(K key, @NotNull Function<? super K, ? extends Short> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(K key, @NotNull ObjShortBiFunction<? super K, ? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.computeIfPresent(key, (K l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(K key, @NotNull ObjShortBiFunction<? super K, ? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short compute(K key, @NotNull BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        return this.compute(key, (K l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(K key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    short update(K key, @NotNull ShortUnaryOperator updateFunction);

    @Nullable
    default Short update(K key, @NotNull UnaryOperator<Short> updateFunction) {
        return this.update(key, (short l_t) -> updateFunction.apply(l_t));
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short merge(K key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        return this.merge(key, value.shortValue(), (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short remapping(K key, @NotNull ObjShortBiFunction<? super K, ? extends K> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default Short remapping(K key, @NotNull BiFunction<? super K, ? super Short, ? extends K> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);
        return this.remapping(key, (K l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
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

    void forEach(ObjShortBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void forEach(BiConsumer<? super K, ? super Short> action) {
        Objects.requireNonNull(action);
        this.forEach((K l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(ObjShortBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default void replaceAll(BiFunction<? super K, ? super Short, ? extends Short> function) {
        Objects.requireNonNull(function);
        this.replaceAll((K l_t, short l_u) -> function.apply(l_t, l_u));
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

    @NotNull ShortCollection values();

    @NotNull SetExt<Entry<K>> rawEntrySet();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     */
    @Deprecated
    default @NotNull SetExt<Map.Entry<K, Short>> entrySet() {
        return (SetExt) this.rawEntrySet();
    }

    interface Entry<K> extends Map.Entry<K, Short> {
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
