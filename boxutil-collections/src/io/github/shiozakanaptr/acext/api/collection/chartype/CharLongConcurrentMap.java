package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface CharLongConcurrentMap extends CharLongMap, ConcurrentMapExt<Character, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return CharLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, long)
     */
    @Deprecated
    default Long putIfAbsent(Character key, Long value) {
        return CharLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, long)
     */
    @Deprecated
    default Long replace(Character key, Long value) {
        return CharLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, long, long)
     */
    @Deprecated
    default boolean replace(Character key, Long oldValue, Long newValue) {
        return CharLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Long> mappingFunction) {
        return CharLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Long, ? extends Long> remappingFunction) {
        return CharLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharLongBiFunction)
     */
    @Deprecated
    default Long compute(Character key, @NotNull BiFunction<? super Character, ? super Long, ? extends Long> remappingFunction) {
        return CharLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Character key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return CharLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Long> action) {
        CharLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Long, ? extends Long> function) {
        CharLongMap.super.replaceAll(function);
    }
}
