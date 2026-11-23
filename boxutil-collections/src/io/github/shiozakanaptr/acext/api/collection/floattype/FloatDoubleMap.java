package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCollection;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface FloatDoubleMap extends MapExt<Float, Double> {
    double get(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(float)
     */
    @Deprecated
    default Double get(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    double getOrDefault(float key, double defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        if (key != null && !(key instanceof Float)) return defaultValue;
        return this.getOrDefault(key != null ? ((Float) key).floatValue() : (float) 0, defaultValue != null ? defaultValue.doubleValue() : (double) 0);
    }

    double put(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, double)
     */
    @Deprecated
    default Double put(Float key, Double value) {
        return this.put(key != null ? key.floatValue() : (float) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double putIfAbsent(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, double)
     */
    @Deprecated
    default Double putIfAbsent(Float key, Double value) {
        return this.putIfAbsent(key != null ? key.floatValue() : (float) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double remove(float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float)
     */
    @Deprecated
    default Double remove(Object key) {
        if (key != null && !(key instanceof Float)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0);
    }

    boolean remove(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.remove(key != null ? ((Float) key).floatValue() : (float) 0, value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double replace(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, double)
     */
    @Deprecated
    default Double replace(Float key, Double value) {
        return this.replace(key != null ? key.floatValue() : (float) 0, value != null ? value.doubleValue() : (double) 0);
    }

    boolean replace(float key, double oldValue, double newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, double, double)
     */
    @Deprecated
    default boolean replace(Float key, Double oldValue, Double newValue) {
        return this.replace(key != null ? key.floatValue() : (float) 0, oldValue != null ? oldValue.doubleValue() : (double) 0, newValue != null ? newValue.doubleValue() : (double) 0);
    }

    double computeIfAbsent(float key, @NotNull FloatFunction<? extends Double> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Double> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.floatValue() : (float) 0, (Float l_t) -> mappingFunction.apply(l_t));
    }

    double computeIfPresent(float key, @NotNull FloatDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
        return this.computeIfPresent(key != null ? key.floatValue() : (float) 0, (float l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double compute(float key, @NotNull FloatDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Float key, @NotNull BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
        return this.compute(key != null ? key.floatValue() : (float) 0, (float l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double merge(float key, double value, @NotNull DoubleDoubleBiFunction<? extends Double> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Float key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return this.merge(key != null ? key.floatValue() : (float) 0, value.doubleValue(), (double l_t, double l_u) -> remappingFunction.apply(l_t, l_u));
    }

    double update(float key, @NotNull DoubleUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(float, DoubleUnaryOperator)
     */
    @Deprecated
    default Double update(Float key, @NotNull Function<? super Double, ? extends Double> updateFunction) {
        return this.update(key != null ? key.floatValue() : (float) 0, (double l_t) -> {
            final Double result;
            return (result = updateFunction.apply(l_t)) != null ? result : (double) 0;
        });
    }

    double remapping(float key, @NotNull FloatDoubleBiFunction<? extends Float> remappingFunction, DoubleDoubleBiFunction<? extends Double> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(float, FloatDoubleBiFunction, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double remapping(Float key, @NotNull BiFunction<? super Float, ? super Double, ? extends Float> remappingFunction, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> mergeFunction) {
        return this.remapping(key != null ? key.floatValue() : (float) 0, (float l_t, double l_u) -> remappingFunction.apply(l_t, l_u), (double l_t, double l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(double)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Double)) return false;
        return this.containsValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    void forEach(@NotNull FloatDoubleBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Double> action) {
        this.forEach((float l_t, double l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull FloatDoubleBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Double, ? extends Double> function) {
        this.replaceAll((float l_t, double l_u) -> function.apply(l_t, l_u));
    }

    double getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Double getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    double setDefaultReturnValue(double newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(double)
     */
    @Deprecated
    default Double setDefaultReturnValue(Double newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.doubleValue() : (double) 0);
    }

    @NotNull
    FloatSet keySet();

    @NotNull
    DoubleCollection values();

    @NotNull
    EntrySetExt<FloatDoubleMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Float, Double>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Float, Double> {
        float getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Float getKey() {
            return this.getKeyRaw();
        }

        double getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Double getValue() {
            return this.getValueRaw();
        }

        double setValue(double newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(double)
         */
        @Deprecated
        default Double setValue(Double value) {
            return this.setValue(value != null ? value.doubleValue() : (double) 0);
        }
    }

}
