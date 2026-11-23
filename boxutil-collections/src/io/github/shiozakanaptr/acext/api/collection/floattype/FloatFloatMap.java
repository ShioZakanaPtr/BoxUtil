package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface FloatFloatMap extends MapExt<Float, Float> {
    float get(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(float)
     */
    @Deprecated
    default Float get(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    float getOrDefault(float key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key != null && !(key instanceof Float)) return defaultValue;
        return this.getOrDefault(key != null ? ((Float) key).floatValue() : (float) 0, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(float key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, float)
     */
    @Deprecated
    default Float put(Float key, Float value) {
        return this.put(key != null ? key.floatValue() : (float) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(float key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, float)
     */
    @Deprecated
    default Float putIfAbsent(Float key, Float value) {
        return this.putIfAbsent(key != null ? key.floatValue() : (float) 0, value != null ? value.floatValue() : (float) 0);
    }

    float remove(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float)
     */
    @Deprecated
    default Float remove(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean remove(float key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(float key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, float)
     */
    @Deprecated
    default Float replace(Float key, Float value) {
        return this.replace(key != null ? key.floatValue() : (float) 0, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(float key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, float, float)
     */
    @Deprecated
    default boolean replace(Float key, Float oldValue, Float newValue) {
        return this.replace(key != null ? key.floatValue() : (float) 0, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsent(float key, @NotNull FloatFunction<? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Float> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.floatValue() : (float) 0, (Float l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(float key, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key != null ? key.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(float key, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float compute(Float key, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key != null ? key.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(float key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Float key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key != null ? key.floatValue() : (float) 0, value != null ? value.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(float key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(float, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(Float key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key != null ? key.floatValue() : (float) 0, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(float key, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(Float key, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key != null ? key.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(float)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Float)) return false;
        return this.containsValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    void forEach(@NotNull FloatFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Float> action) {
        this.forEach((float l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull FloatFloatBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Float, ? extends Float> function) {
        this.replaceAll((float l_t, float l_u) -> function.apply(l_t, l_u));
    }

    float getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Float getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    float setDefaultReturnValue(float newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(float)
     */
    @Deprecated
    default Float setDefaultReturnValue(Float newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.floatValue() : (float) 0);
    }

    @NotNull
    FloatSet keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<FloatFloatMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Float, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Float, Float> {
        float getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Float getKey() {
            return this.getKeyRaw();
        }

        float getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Float getValue() {
            return this.getValueRaw();
        }

        float setValue(float newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(float)
         */
        @Deprecated
        default Float setValue(Float value) {
            return this.setValue(value != null ? value.floatValue() : (float) 0);
        }
    }

}
