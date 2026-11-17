package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FloatFloatConcurrentMap extends FloatFloatMap, ConcurrentMapExt<Float, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return FloatFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, float)
     */
    @Deprecated
    default Float putIfAbsent(Float key, Float value) {
        return FloatFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, float)
     */
    @Deprecated
    default Float replace(Float key, Float value) {
        return FloatFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, float, float)
     */
    @Deprecated
    default boolean replace(Float key, Float oldValue, Float newValue) {
        return FloatFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Float> mappingFunction) {
        return FloatFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return FloatFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float compute(Float key, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return FloatFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Float key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return FloatFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Float> action) {
        FloatFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Float, ? extends Float> function) {
        FloatFloatMap.super.replaceAll(function);
    }
}
