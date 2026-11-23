package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCollection;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface DoubleFloatMap extends MapExt<Double, Float> {
    float get(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(double)
     */
    @Deprecated
    default Float get(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    float getOrDefault(double key, float defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        if (key != null && !(key instanceof Double)) return defaultValue;
        return this.getOrDefault(key != null ? ((Double) key).doubleValue() : (double) 0, defaultValue != null ? defaultValue.floatValue() : (float) 0);
    }

    float put(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, float)
     */
    @Deprecated
    default Float put(Double key, Float value) {
        return this.put(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0);
    }

    float putIfAbsent(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, float)
     */
    @Deprecated
    default Float putIfAbsent(Double key, Float value) {
        return this.putIfAbsent(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0);
    }

    float remove(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double)
     */
    @Deprecated
    default Float remove(Object key) {
        if (key != null && !(key instanceof Double)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    boolean remove(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.remove(key != null ? ((Double) key).doubleValue() : (double) 0, value != null ? ((Float) value).floatValue() : (float) 0);
    }

    float replace(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, float)
     */
    @Deprecated
    default Float replace(Double key, Float value) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0);
    }

    boolean replace(double key, float oldValue, float newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, float, float)
     */
    @Deprecated
    default boolean replace(Double key, Float oldValue, Float newValue) {
        return this.replace(key != null ? key.doubleValue() : (double) 0, oldValue != null ? oldValue.floatValue() : (float) 0, newValue != null ? newValue.floatValue() : (float) 0);
    }

    float computeIfAbsent(double key, @NotNull DoubleFunction<? extends Float> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Float> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.doubleValue() : (double) 0, (Double l_t) -> mappingFunction.apply(l_t));
    }

    float computeIfPresent(double key, @NotNull DoubleFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Float, ? extends Float> remappingFunction) {
        return this.computeIfPresent(key != null ? key.doubleValue() : (double) 0, (double l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float compute(double key, @NotNull DoubleFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleFloatBiFunction)
     */
    @Deprecated
    default Float compute(Double key, @NotNull BiFunction<? super Double, ? super Float, ? extends Float> remappingFunction) {
        return this.compute(key != null ? key.doubleValue() : (double) 0, (double l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float merge(double key, float value, @NotNull FloatFloatBiFunction<? extends Float> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Double key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return this.merge(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0, (float l_t, float l_u) -> remappingFunction.apply(l_t, l_u));
    }

    float update(double key, @NotNull FloatUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(double, FloatUnaryOperator)
     */
    @Deprecated
    default Float update(Double key, @NotNull Function<? super Float, ? extends Float> updateFunction) {
        return this.update(key != null ? key.doubleValue() : (double) 0, (float l_t) -> {
            final Float result;
            return (result = updateFunction.apply(l_t)) != null ? result : (float) 0;
        });
    }

    float remapping(double key, @NotNull DoubleFloatBiFunction<? extends Double> remappingFunction, FloatFloatBiFunction<? extends Float> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(double, DoubleFloatBiFunction, FloatFloatBiFunction)
     */
    @Deprecated
    default Float remapping(Double key, @NotNull BiFunction<? super Double, ? super Float, ? extends Double> remappingFunction, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> mergeFunction) {
        return this.remapping(key != null ? key.doubleValue() : (double) 0, (double l_t, float l_u) -> remappingFunction.apply(l_t, l_u), (float l_t, float l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(double from, double to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(double, double)
     */
    @Deprecated
    default boolean swapAt(Double from, Double to) {
        return this.swapAt(from != null ? from.doubleValue() : (double) 0, to != null ? to.doubleValue() : (double) 0);
    }

    boolean containsKey(double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(double)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Double)) return false;
        return this.containsKey(key != null ? ((Double) key).doubleValue() : (double) 0);
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

    void forEach(@NotNull DoubleFloatBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Float> action) {
        this.forEach((double l_t, float l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull DoubleFloatBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Float, ? extends Float> function) {
        this.replaceAll((double l_t, float l_u) -> function.apply(l_t, l_u));
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
    DoubleSet keySet();

    @NotNull
    FloatCollection values();

    @NotNull
    EntrySetExt<DoubleFloatMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Double, Float>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Double, Float> {
        double getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Double getKey() {
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
