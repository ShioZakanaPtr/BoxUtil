package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
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
public interface ShortShortConcurrentMap extends ShortShortMap, ConcurrentMapExt<Short, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return ShortShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, short)
     */
    @Deprecated
    default Short putIfAbsent(Short key, Short value) {
        return ShortShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, short)
     */
    @Deprecated
    default Short replace(Short key, Short value) {
        return ShortShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, short, short)
     */
    @Deprecated
    default boolean replace(Short key, Short oldValue, Short newValue) {
        return ShortShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Short> mappingFunction) {
        return ShortShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return ShortShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortShortBiFunction)
     */
    @Deprecated
    default Short compute(Short key, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return ShortShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Short key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return ShortShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Short> action) {
        ShortShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Short, ? extends Short> function) {
        ShortShortMap.super.replaceAll(function);
    }
}
