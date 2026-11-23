package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface CharFloatConcurrentMap extends CharFloatMap, ConcurrentMapExt<Character, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return CharFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, float)
     */
    @Deprecated
    default Float putIfAbsent(Character key, Float value) {
        return CharFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, float)
     */
    @Deprecated
    default Float replace(Character key, Float value) {
        return CharFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, float, float)
     */
    @Deprecated
    default boolean replace(Character key, Float oldValue, Float newValue) {
        return CharFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Float> mappingFunction) {
        return CharFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Float, ? extends Float> remappingFunction) {
        return CharFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharFloatBiFunction)
     */
    @Deprecated
    default Float compute(Character key, @NotNull BiFunction<? super Character, ? super Float, ? extends Float> remappingFunction) {
        return CharFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Character key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return CharFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Float> action) {
        CharFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Float, ? extends Float> function) {
        CharFloatMap.super.replaceAll(function);
    }
}
