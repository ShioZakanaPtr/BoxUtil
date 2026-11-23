package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @see ConcurrentMapExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface DoubleFloatConcurrentMap extends DoubleFloatMap, ConcurrentMapExt<Double, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return DoubleFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, float)
     */
    @Deprecated
    default Float putIfAbsent(Double key, Float value) {
        return DoubleFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, float)
     */
    @Deprecated
    default Float replace(Double key, Float value) {
        return DoubleFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, float, float)
     */
    @Deprecated
    default boolean replace(Double key, Float oldValue, Float newValue) {
        return DoubleFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Float> mappingFunction) {
        return DoubleFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Float, ? extends Float> remappingFunction) {
        return DoubleFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleFloatBiFunction)
     */
    @Deprecated
    default Float compute(Double key, @NotNull BiFunction<? super Double, ? super Float, ? extends Float> remappingFunction) {
        return DoubleFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Double key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return DoubleFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Float> action) {
        DoubleFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Float, ? extends Float> function) {
        DoubleFloatMap.super.replaceAll(function);
    }
}
