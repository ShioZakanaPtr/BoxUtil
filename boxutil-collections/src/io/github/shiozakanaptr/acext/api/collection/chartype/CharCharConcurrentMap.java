package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CharCharConcurrentMap extends CharCharMap, ConcurrentMapExt<Character, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return CharCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, char)
     */
    @Deprecated
    default Character putIfAbsent(Character key, Character value) {
        return CharCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, char)
     */
    @Deprecated
    default Character replace(Character key, Character value) {
        return CharCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, char, char)
     */
    @Deprecated
    default boolean replace(Character key, Character oldValue, Character newValue) {
        return CharCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Character> mappingFunction) {
        return CharCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return CharCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharCharBiFunction)
     */
    @Deprecated
    default Character compute(Character key, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return CharCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Character key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return CharCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Character> action) {
        CharCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Character, ? extends Character> function) {
        CharCharMap.super.replaceAll(function);
    }
}
