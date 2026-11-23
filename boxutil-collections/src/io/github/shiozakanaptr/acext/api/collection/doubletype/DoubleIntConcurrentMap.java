package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface DoubleIntConcurrentMap extends DoubleIntMap, ConcurrentMapExt<Double, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return DoubleIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, int)
     */
    @Deprecated
    default Integer putIfAbsent(Double key, Integer value) {
        return DoubleIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, int)
     */
    @Deprecated
    default Integer replace(Double key, Integer value) {
        return DoubleIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, int, int)
     */
    @Deprecated
    default boolean replace(Double key, Integer oldValue, Integer newValue) {
        return DoubleIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Integer> mappingFunction) {
        return DoubleIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
        return DoubleIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleIntBiFunction)
     */
    @Deprecated
    default Integer compute(Double key, @NotNull BiFunction<? super Double, ? super Integer, ? extends Integer> remappingFunction) {
        return DoubleIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Double key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return DoubleIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Integer> action) {
        DoubleIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Integer, ? extends Integer> function) {
        DoubleIntMap.super.replaceAll(function);
    }
}
