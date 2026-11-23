package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface FloatLongConcurrentMap extends FloatLongMap, ConcurrentMapExt<Float, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return FloatLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, long)
     */
    @Deprecated
    default Long putIfAbsent(Float key, Long value) {
        return FloatLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, long)
     */
    @Deprecated
    default Long replace(Float key, Long value) {
        return FloatLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, long, long)
     */
    @Deprecated
    default boolean replace(Float key, Long oldValue, Long newValue) {
        return FloatLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Long> mappingFunction) {
        return FloatLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Long, ? extends Long> remappingFunction) {
        return FloatLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatLongBiFunction)
     */
    @Deprecated
    default Long compute(Float key, @NotNull BiFunction<? super Float, ? super Long, ? extends Long> remappingFunction) {
        return FloatLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Float key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return FloatLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Long> action) {
        FloatLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Long, ? extends Long> function) {
        FloatLongMap.super.replaceAll(function);
    }
}
