package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DoubleShortConcurrentMap extends DoubleShortMap, ConcurrentMapExt<Double, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return DoubleShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, short)
     */
    @Deprecated
    default Short putIfAbsent(Double key, Short value) {
        return DoubleShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, short)
     */
    @Deprecated
    default Short replace(Double key, Short value) {
        return DoubleShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, short, short)
     */
    @Deprecated
    default boolean replace(Double key, Short oldValue, Short newValue) {
        return DoubleShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Short> mappingFunction) {
        return DoubleShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Short, ? extends Short> remappingFunction) {
        return DoubleShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleShortBiFunction)
     */
    @Deprecated
    default Short compute(Double key, @NotNull BiFunction<? super Double, ? super Short, ? extends Short> remappingFunction) {
        return DoubleShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Double key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return DoubleShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Short> action) {
        DoubleShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Short, ? extends Short> function) {
        DoubleShortMap.super.replaceAll(function);
    }
}
