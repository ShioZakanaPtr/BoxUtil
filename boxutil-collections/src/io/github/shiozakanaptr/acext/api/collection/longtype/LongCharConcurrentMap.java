package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface LongCharConcurrentMap extends LongCharMap, ConcurrentMapExt<Long, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return LongCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, char)
     */
    @Deprecated
    default Character putIfAbsent(Long key, Character value) {
        return LongCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, char)
     */
    @Deprecated
    default Character replace(Long key, Character value) {
        return LongCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, char, char)
     */
    @Deprecated
    default boolean replace(Long key, Character oldValue, Character newValue) {
        return LongCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Character> mappingFunction) {
        return LongCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Character, ? extends Character> remappingFunction) {
        return LongCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongCharBiFunction)
     */
    @Deprecated
    default Character compute(Long key, @NotNull BiFunction<? super Long, ? super Character, ? extends Character> remappingFunction) {
        return LongCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Long key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return LongCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Character> action) {
        LongCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Character, ? extends Character> function) {
        LongCharMap.super.replaceAll(function);
    }
}
