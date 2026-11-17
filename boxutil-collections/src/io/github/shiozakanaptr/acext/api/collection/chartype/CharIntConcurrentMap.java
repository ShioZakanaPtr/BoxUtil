package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CharIntConcurrentMap extends CharIntMap, ConcurrentMapExt<Character, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return CharIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, int)
     */
    @Deprecated
    default Integer putIfAbsent(Character key, Integer value) {
        return CharIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, int)
     */
    @Deprecated
    default Integer replace(Character key, Integer value) {
        return CharIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, int, int)
     */
    @Deprecated
    default boolean replace(Character key, Integer oldValue, Integer newValue) {
        return CharIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Integer> mappingFunction) {
        return CharIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Integer, ? extends Integer> remappingFunction) {
        return CharIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharIntBiFunction)
     */
    @Deprecated
    default Integer compute(Character key, @NotNull BiFunction<? super Character, ? super Integer, ? extends Integer> remappingFunction) {
        return CharIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Character key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return CharIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        CharIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Integer, ? extends Integer> function) {
        CharIntMap.super.replaceAll(function);
    }
}
