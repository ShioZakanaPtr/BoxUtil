package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LongByteConcurrentMap extends LongByteMap, ConcurrentMapExt<Long, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return LongByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Long key, Byte value) {
        return LongByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, byte)
     */
    @Deprecated
    default Byte replace(Long key, Byte value) {
        return LongByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, byte, byte)
     */
    @Deprecated
    default boolean replace(Long key, Byte oldValue, Byte newValue) {
        return LongByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Byte> mappingFunction) {
        return LongByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> remappingFunction) {
        return LongByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongByteBiFunction)
     */
    @Deprecated
    default Byte compute(Long key, @NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> remappingFunction) {
        return LongByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Long key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return LongByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Byte> action) {
        LongByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Byte, ? extends Byte> function) {
        LongByteMap.super.replaceAll(function);
    }
}
