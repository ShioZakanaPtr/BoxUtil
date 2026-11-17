package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CharByteConcurrentMap extends CharByteMap, ConcurrentMapExt<Character, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return CharByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Character key, Byte value) {
        return CharByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, byte)
     */
    @Deprecated
    default Byte replace(Character key, Byte value) {
        return CharByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, byte, byte)
     */
    @Deprecated
    default boolean replace(Character key, Byte oldValue, Byte newValue) {
        return CharByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Byte> mappingFunction) {
        return CharByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Byte, ? extends Byte> remappingFunction) {
        return CharByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharByteBiFunction)
     */
    @Deprecated
    default Byte compute(Character key, @NotNull BiFunction<? super Character, ? super Byte, ? extends Byte> remappingFunction) {
        return CharByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Character key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return CharByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        CharByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Byte, ? extends Byte> function) {
        CharByteMap.super.replaceAll(function);
    }
}
