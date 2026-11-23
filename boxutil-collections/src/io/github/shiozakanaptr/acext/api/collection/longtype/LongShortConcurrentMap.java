package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface LongShortConcurrentMap extends LongShortMap, ConcurrentMapExt<Long, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return LongShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, short)
     */
    @Deprecated
    default Short putIfAbsent(Long key, Short value) {
        return LongShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, short)
     */
    @Deprecated
    default Short replace(Long key, Short value) {
        return LongShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, short, short)
     */
    @Deprecated
    default boolean replace(Long key, Short oldValue, Short newValue) {
        return LongShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Short> mappingFunction) {
        return LongShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        return LongShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongShortBiFunction)
     */
    @Deprecated
    default Short compute(Long key, @NotNull BiFunction<? super Long, ? super Short, ? extends Short> remappingFunction) {
        return LongShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Long key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return LongShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Short> action) {
        LongShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Short, ? extends Short> function) {
        LongShortMap.super.replaceAll(function);
    }
}
