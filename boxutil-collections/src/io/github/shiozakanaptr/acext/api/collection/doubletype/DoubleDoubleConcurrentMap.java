package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DoubleDoubleConcurrentMap extends DoubleDoubleMap, ConcurrentMapExt<Double, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return DoubleDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, double)
     */
    @Deprecated
    default Double putIfAbsent(Double key, Double value) {
        return DoubleDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, double)
     */
    @Deprecated
    default Double replace(Double key, Double value) {
        return DoubleDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, double, double)
     */
    @Deprecated
    default boolean replace(Double key, Double oldValue, Double newValue) {
        return DoubleDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Double> mappingFunction) {
        return DoubleDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return DoubleDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Double key, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return DoubleDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Double key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return DoubleDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Double> action) {
        DoubleDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Double, ? extends Double> function) {
        DoubleDoubleMap.super.replaceAll(function);
    }
}
