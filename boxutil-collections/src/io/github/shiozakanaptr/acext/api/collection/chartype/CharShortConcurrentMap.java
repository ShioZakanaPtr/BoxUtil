package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface CharShortConcurrentMap extends CharShortMap, ConcurrentMapExt<Character, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return CharShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, short)
     */
    @Deprecated
    default Short putIfAbsent(Character key, Short value) {
        return CharShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, short)
     */
    @Deprecated
    default Short replace(Character key, Short value) {
        return CharShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, short, short)
     */
    @Deprecated
    default boolean replace(Character key, Short oldValue, Short newValue) {
        return CharShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Short> mappingFunction) {
        return CharShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
        return CharShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharShortBiFunction)
     */
    @Deprecated
    default Short compute(Character key, @NotNull BiFunction<? super Character, ? super Short, ? extends Short> remappingFunction) {
        return CharShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Character key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return CharShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Short> action) {
        CharShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Short, ? extends Short> function) {
        CharShortMap.super.replaceAll(function);
    }
}
