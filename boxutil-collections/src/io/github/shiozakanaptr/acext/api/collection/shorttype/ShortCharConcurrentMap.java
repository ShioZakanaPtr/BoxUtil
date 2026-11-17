package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ShortCharConcurrentMap extends ShortCharMap, ConcurrentMapExt<Short, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return ShortCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, char)
     */
    @Deprecated
    default Character putIfAbsent(Short key, Character value) {
        return ShortCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, char)
     */
    @Deprecated
    default Character replace(Short key, Character value) {
        return ShortCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, char, char)
     */
    @Deprecated
    default boolean replace(Short key, Character oldValue, Character newValue) {
        return ShortCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Character> mappingFunction) {
        return ShortCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Character, ? extends Character> remappingFunction) {
        return ShortCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortCharBiFunction)
     */
    @Deprecated
    default Character compute(Short key, @NotNull BiFunction<? super Short, ? super Character, ? extends Character> remappingFunction) {
        return ShortCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Short key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return ShortCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Character> action) {
        ShortCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Character, ? extends Character> function) {
        ShortCharMap.super.replaceAll(function);
    }
}
