package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface LongFloatConcurrentMap extends LongFloatMap, ConcurrentMapExt<Long, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return LongFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, float)
     */
    @Deprecated
    default Float putIfAbsent(Long key, Float value) {
        return LongFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, float)
     */
    @Deprecated
    default Float replace(Long key, Float value) {
        return LongFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, float, float)
     */
    @Deprecated
    default boolean replace(Long key, Float oldValue, Float newValue) {
        return LongFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Float> mappingFunction) {
        return LongFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        return LongFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongFloatBiFunction)
     */
    @Deprecated
    default Float compute(Long key, @NotNull BiFunction<? super Long, ? super Float, ? extends Float> remappingFunction) {
        return LongFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Long key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return LongFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Float> action) {
        LongFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Float, ? extends Float> function) {
        LongFloatMap.super.replaceAll(function);
    }
}
