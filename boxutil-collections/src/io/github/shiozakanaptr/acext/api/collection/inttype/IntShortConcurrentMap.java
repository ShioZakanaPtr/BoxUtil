package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface IntShortConcurrentMap extends IntShortMap, ConcurrentMapExt<Integer, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return IntShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, short)
     */
    @Deprecated
    default Short putIfAbsent(Integer key, Short value) {
        return IntShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, short)
     */
    @Deprecated
    default Short replace(Integer key, Short value) {
        return IntShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, short, short)
     */
    @Deprecated
    default boolean replace(Integer key, Short oldValue, Short newValue) {
        return IntShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Short> mappingFunction) {
        return IntShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        return IntShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntShortBiFunction)
     */
    @Deprecated
    default Short compute(Integer key, @NotNull BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
        return IntShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Integer key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return IntShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Short> action) {
        IntShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Short, ? extends Short> function) {
        IntShortMap.super.replaceAll(function);
    }
}
