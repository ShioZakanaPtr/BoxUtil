package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ShortByteConcurrentMap extends ShortByteMap, ConcurrentMapExt<Short, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return ShortByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Short key, Byte value) {
        return ShortByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, byte)
     */
    @Deprecated
    default Byte replace(Short key, Byte value) {
        return ShortByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, byte, byte)
     */
    @Deprecated
    default boolean replace(Short key, Byte oldValue, Byte newValue) {
        return ShortByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Byte> mappingFunction) {
        return ShortByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        return ShortByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortByteBiFunction)
     */
    @Deprecated
    default Byte compute(Short key, @NotNull BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        return ShortByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Short key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return ShortByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Byte> action) {
        ShortByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Byte, ? extends Byte> function) {
        ShortByteMap.super.replaceAll(function);
    }
}
