package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface DoubleCharConcurrentMap extends DoubleCharMap, ConcurrentMapExt<Double, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return DoubleCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, char)
     */
    @Deprecated
    default Character putIfAbsent(Double key, Character value) {
        return DoubleCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, char)
     */
    @Deprecated
    default Character replace(Double key, Character value) {
        return DoubleCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, char, char)
     */
    @Deprecated
    default boolean replace(Double key, Character oldValue, Character newValue) {
        return DoubleCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Character> mappingFunction) {
        return DoubleCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
        return DoubleCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleCharBiFunction)
     */
    @Deprecated
    default Character compute(Double key, @NotNull BiFunction<? super Double, ? super Character, ? extends Character> remappingFunction) {
        return DoubleCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Double key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return DoubleCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Character> action) {
        DoubleCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Character, ? extends Character> function) {
        DoubleCharMap.super.replaceAll(function);
    }
}
