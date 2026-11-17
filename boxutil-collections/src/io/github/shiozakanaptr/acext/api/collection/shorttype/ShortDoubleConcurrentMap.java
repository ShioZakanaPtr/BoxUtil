package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ShortDoubleConcurrentMap extends ShortDoubleMap, ConcurrentMapExt<Short, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return ShortDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, double)
     */
    @Deprecated
    default Double putIfAbsent(Short key, Double value) {
        return ShortDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, double)
     */
    @Deprecated
    default Double replace(Short key, Double value) {
        return ShortDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, double, double)
     */
    @Deprecated
    default boolean replace(Short key, Double oldValue, Double newValue) {
        return ShortDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Double> mappingFunction) {
        return ShortDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
        return ShortDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Short key, @NotNull BiFunction<? super Short, ? super Double, ? extends Double> remappingFunction) {
        return ShortDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Short key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return ShortDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Double> action) {
        ShortDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Double, ? extends Double> function) {
        ShortDoubleMap.super.replaceAll(function);
    }
}
