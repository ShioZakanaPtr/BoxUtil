package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FloatCharConcurrentMap extends FloatCharMap, ConcurrentMapExt<Float, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return FloatCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, char)
     */
    @Deprecated
    default Character putIfAbsent(Float key, Character value) {
        return FloatCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, char)
     */
    @Deprecated
    default Character replace(Float key, Character value) {
        return FloatCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, char, char)
     */
    @Deprecated
    default boolean replace(Float key, Character oldValue, Character newValue) {
        return FloatCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Character> mappingFunction) {
        return FloatCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
        return FloatCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatCharBiFunction)
     */
    @Deprecated
    default Character compute(Float key, @NotNull BiFunction<? super Float, ? super Character, ? extends Character> remappingFunction) {
        return FloatCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Float key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return FloatCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Character> action) {
        FloatCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Character, ? extends Character> function) {
        FloatCharMap.super.replaceAll(function);
    }
}
