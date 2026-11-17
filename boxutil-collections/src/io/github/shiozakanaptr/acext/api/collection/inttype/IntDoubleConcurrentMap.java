package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntDoubleConcurrentMap extends IntDoubleMap, ConcurrentMapExt<Integer, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return IntDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, double)
     */
    @Deprecated
    default Double putIfAbsent(Integer key, Double value) {
        return IntDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, double)
     */
    @Deprecated
    default Double replace(Integer key, Double value) {
        return IntDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, double, double)
     */
    @Deprecated
    default boolean replace(Integer key, Double oldValue, Double newValue) {
        return IntDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Double> mappingFunction) {
        return IntDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Double, ? extends Double> remappingFunction) {
        return IntDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Integer key, @NotNull BiFunction<? super Integer, ? super Double, ? extends Double> remappingFunction) {
        return IntDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Integer key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return IntDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Double> action) {
        IntDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Double, ? extends Double> function) {
        IntDoubleMap.super.replaceAll(function);
    }
}
