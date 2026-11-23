package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface CharDoubleConcurrentMap extends CharDoubleMap, ConcurrentMapExt<Character, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return CharDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, double)
     */
    @Deprecated
    default Double putIfAbsent(Character key, Double value) {
        return CharDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, double)
     */
    @Deprecated
    default Double replace(Character key, Double value) {
        return CharDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, double, double)
     */
    @Deprecated
    default boolean replace(Character key, Double oldValue, Double newValue) {
        return CharDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Double> mappingFunction) {
        return CharDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
        return CharDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Character key, @NotNull BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
        return CharDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Character key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return CharDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Double> action) {
        CharDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Double, ? extends Double> function) {
        CharDoubleMap.super.replaceAll(function);
    }
}
