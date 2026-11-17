package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FloatDoubleConcurrentMap extends FloatDoubleMap, ConcurrentMapExt<Float, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return FloatDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, double)
     */
    @Deprecated
    default Double putIfAbsent(Float key, Double value) {
        return FloatDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, double)
     */
    @Deprecated
    default Double replace(Float key, Double value) {
        return FloatDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, double, double)
     */
    @Deprecated
    default boolean replace(Float key, Double oldValue, Double newValue) {
        return FloatDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Double> mappingFunction) {
        return FloatDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
        return FloatDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Float key, @NotNull BiFunction<? super Float, ? super Double, ? extends Double> remappingFunction) {
        return FloatDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Float key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return FloatDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Double> action) {
        FloatDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Double, ? extends Double> function) {
        FloatDoubleMap.super.replaceAll(function);
    }
}
