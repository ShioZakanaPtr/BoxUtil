package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
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
public interface FloatByteConcurrentMap extends FloatByteMap, ConcurrentMapExt<Float, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return FloatByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Float key, Byte value) {
        return FloatByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, byte)
     */
    @Deprecated
    default Byte replace(Float key, Byte value) {
        return FloatByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, byte, byte)
     */
    @Deprecated
    default boolean replace(Float key, Byte oldValue, Byte newValue) {
        return FloatByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Byte> mappingFunction) {
        return FloatByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) {
        return FloatByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatByteBiFunction)
     */
    @Deprecated
    default Byte compute(Float key, @NotNull BiFunction<? super Float, ? super Byte, ? extends Byte> remappingFunction) {
        return FloatByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Float key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return FloatByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Byte> action) {
        FloatByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Byte, ? extends Byte> function) {
        FloatByteMap.super.replaceAll(function);
    }
}
