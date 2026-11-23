package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

/**
 * Type specialization interface.
 *
 * @see MapExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface FloatShortMap extends MapExt<Float, Short> {
    short get(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(float)
     */
    @Deprecated
    default Short get(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    short getOrDefault(float key, short defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        if (key != null && !(key instanceof Float)) return defaultValue;
        return this.getOrDefault(key != null ? ((Float) key).floatValue() : (float) 0, defaultValue != null ? defaultValue.shortValue() : (short) 0);
    }

    short put(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, short)
     */
    @Deprecated
    default Short put(Float key, Short value) {
        return this.put(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0);
    }

    short putIfAbsent(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, short)
     */
    @Deprecated
    default Short putIfAbsent(Float key, Short value) {
        return this.putIfAbsent(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0);
    }

    short remove(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float)
     */
    @Deprecated
    default Short remove(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean remove(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0, value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short replace(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, short)
     */
    @Deprecated
    default Short replace(Float key, Short value) {
        return this.replace(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0);
    }

    boolean replace(float key, short oldValue, short newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, short, short)
     */
    @Deprecated
    default boolean replace(Float key, Short oldValue, Short newValue) {
        return this.replace(key != null ? key.floatValue() : (float) 0, oldValue != null ? oldValue.shortValue() : (short) 0, newValue != null ? newValue.shortValue() : (short) 0);
    }

    short computeIfAbsent(float key, @NotNull FloatFunction<? extends Short> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Short> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.floatValue() : (float) 0, (Float l_t) -> mappingFunction.apply(l_t));
    }

    short computeIfPresent(float key, @NotNull FloatShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
        return this.computeIfPresent(key != null ? key.floatValue() : (float) 0, (float l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short compute(float key, @NotNull FloatShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatShortBiFunction)
     */
    @Deprecated
    default Short compute(Float key, @NotNull BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
        return this.compute(key != null ? key.floatValue() : (float) 0, (float l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short merge(float key, short value, @NotNull ShortShortBiFunction<? extends Short> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Float key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return this.merge(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0, (short l_t, short l_u) -> remappingFunction.apply(l_t, l_u));
    }

    short update(float key, @NotNull ShortUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(float, ShortUnaryOperator)
     */
    @Deprecated
    default Short update(Float key, @NotNull Function<? super Short, ? extends Short> updateFunction) {
        return this.update(key != null ? key.floatValue() : (float) 0, (short l_t) -> {
            final Short result;
            return (result = updateFunction.apply(l_t)) != null ? result : (short) 0;
        });
    }

    short remapping(float key, @NotNull FloatShortBiFunction<? extends Float> remappingFunction, ShortShortBiFunction<? extends Short> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatShortBiFunction, ShortShortBiFunction)
     */
    @Deprecated
    default Short remapping(Float key, @NotNull BiFunction<? super Float, ? super Short, ? extends Float> remappingFunction, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> mergeFunction) {
        return this.remapping(key != null ? key.floatValue() : (float) 0, (float l_t, short l_u) -> remappingFunction.apply(l_t, l_u), (short l_t, short l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(float from, float to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(float, float)
     */
    @Deprecated
    default boolean swapAt(Float from, Float to) {
        return this.swapAt(from != null ? from.floatValue() : (float) 0, to != null ? to.floatValue() : (float) 0);
    }

    boolean containsKey(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(float)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Float)) return false;
        return this.containsKey(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean containsValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(short)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Short)) return false;
        return this.containsValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    void forEach(@NotNull FloatShortBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Short> action) {
        this.forEach((float l_t, short l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull FloatShortBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Short, ? extends Short> function) {
        this.replaceAll((float l_t, short l_u) -> function.apply(l_t, l_u));
    }

    short getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Short getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    short setDefaultReturnValue(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(short)
     */
    @Deprecated
    default Short setDefaultReturnValue(Short newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull
    FloatSet keySet();

    @NotNull
    ShortCollection values();

    @NotNull
    EntrySetExt<FloatShortMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Float, Short>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Float, Short> {
        float getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Float getKey() {
            return this.getKeyRaw();
        }

        short getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Short getValue() {
            return this.getValueRaw();
        }

        short setValue(short newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(short)
         */
        @Deprecated
        default Short setValue(Short value) {
            return this.setValue(value != null ? value.shortValue() : (short) 0);
        }
    }

}
