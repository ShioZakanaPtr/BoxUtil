package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ByteByteConcurrentMap extends ByteByteMap, ConcurrentMapExt<Byte, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return ByteByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Byte key, Byte value) {
        return ByteByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, byte)
     */
    @Deprecated
    default Byte replace(Byte key, Byte value) {
        return ByteByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, byte, byte)
     */
    @Deprecated
    default boolean replace(Byte key, Byte oldValue, Byte newValue) {
        return ByteByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Byte> mappingFunction) {
        return ByteByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return ByteByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte compute(Byte key, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return ByteByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Byte key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return ByteByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Byte> action) {
        ByteByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> function) {
        ByteByteMap.super.replaceAll(function);
    }
}
