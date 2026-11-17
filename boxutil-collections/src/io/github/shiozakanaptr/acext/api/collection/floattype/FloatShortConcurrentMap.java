package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FloatShortConcurrentMap extends FloatShortMap, ConcurrentMapExt<Float, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return FloatShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, short)
     */
    @Deprecated
    default Short putIfAbsent(Float key, Short value) {
        return FloatShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, short)
     */
    @Deprecated
    default Short replace(Float key, Short value) {
        return FloatShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, short, short)
     */
    @Deprecated
    default boolean replace(Float key, Short oldValue, Short newValue) {
        return FloatShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Short> mappingFunction) {
        return FloatShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
        return FloatShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatShortBiFunction)
     */
    @Deprecated
    default Short compute(Float key, @NotNull BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
        return FloatShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Float key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return FloatShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Short> action) {
        FloatShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Short, ? extends Short> function) {
        FloatShortMap.super.replaceAll(function);
    }
}
