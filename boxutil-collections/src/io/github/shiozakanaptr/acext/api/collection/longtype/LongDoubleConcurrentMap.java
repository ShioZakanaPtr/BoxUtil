package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface LongDoubleConcurrentMap extends LongDoubleMap, ConcurrentMapExt<Long, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return LongDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, double)
     */
    @Deprecated
    default Double putIfAbsent(Long key, Double value) {
        return LongDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, double)
     */
    @Deprecated
    default Double replace(Long key, Double value) {
        return LongDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, double, double)
     */
    @Deprecated
    default boolean replace(Long key, Double oldValue, Double newValue) {
        return LongDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Double> mappingFunction) {
        return LongDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Double, ? extends Double> remappingFunction) {
        return LongDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Long key, @NotNull BiFunction<? super Long, ? super Double, ? extends Double> remappingFunction) {
        return LongDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Long key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return LongDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Double> action) {
        LongDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Double, ? extends Double> function) {
        LongDoubleMap.super.replaceAll(function);
    }
}
