package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntCharConcurrentMap extends IntCharMap, ConcurrentMapExt<Integer, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return IntCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, char)
     */
    @Deprecated
    default Character putIfAbsent(Integer key, Character value) {
        return IntCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, char)
     */
    @Deprecated
    default Character replace(Integer key, Character value) {
        return IntCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, char, char)
     */
    @Deprecated
    default boolean replace(Integer key, Character oldValue, Character newValue) {
        return IntCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Character> mappingFunction) {
        return IntCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Character, ? extends Character> remappingFunction) {
        return IntCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntCharBiFunction)
     */
    @Deprecated
    default Character compute(Integer key, @NotNull BiFunction<? super Integer, ? super Character, ? extends Character> remappingFunction) {
        return IntCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Integer key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return IntCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        IntCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Character, ? extends Character> function) {
        IntCharMap.super.replaceAll(function);
    }
}
