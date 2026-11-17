package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DoubleLongConcurrentMap extends DoubleLongMap, ConcurrentMapExt<Double, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return DoubleLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, long)
     */
    @Deprecated
    default Long putIfAbsent(Double key, Long value) {
        return DoubleLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, long)
     */
    @Deprecated
    default Long replace(Double key, Long value) {
        return DoubleLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, long, long)
     */
    @Deprecated
    default boolean replace(Double key, Long oldValue, Long newValue) {
        return DoubleLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Long> mappingFunction) {
        return DoubleLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        return DoubleLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleLongBiFunction)
     */
    @Deprecated
    default Long compute(Double key, @NotNull BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
        return DoubleLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Double key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return DoubleLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Long> action) {
        DoubleLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Long, ? extends Long> function) {
        DoubleLongMap.super.replaceAll(function);
    }
}
