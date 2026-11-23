package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
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
public interface ByteCharConcurrentMap extends ByteCharMap, ConcurrentMapExt<Byte, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return ByteCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, char)
     */
    @Deprecated
    default Character putIfAbsent(Byte key, Character value) {
        return ByteCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, char)
     */
    @Deprecated
    default Character replace(Byte key, Character value) {
        return ByteCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, char, char)
     */
    @Deprecated
    default boolean replace(Byte key, Character oldValue, Character newValue) {
        return ByteCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Character> mappingFunction) {
        return ByteCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
        return ByteCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteCharBiFunction)
     */
    @Deprecated
    default Character compute(Byte key, @NotNull BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
        return ByteCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Byte key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return ByteCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        ByteCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Character, ? extends Character> function) {
        ByteCharMap.super.replaceAll(function);
    }
}
