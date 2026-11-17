package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntByteConcurrentMap extends IntByteMap, ConcurrentMapExt<Integer, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return IntByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Integer key, Byte value) {
        return IntByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, byte)
     */
    @Deprecated
    default Byte replace(Integer key, Byte value) {
        return IntByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, byte, byte)
     */
    @Deprecated
    default boolean replace(Integer key, Byte oldValue, Byte newValue) {
        return IntByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Byte> mappingFunction) {
        return IntByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return IntByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntByteBiFunction)
     */
    @Deprecated
    default Byte compute(Integer key, @NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> remappingFunction) {
        return IntByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Integer key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return IntByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Byte> action) {
        IntByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Byte, ? extends Byte> function) {
        IntByteMap.super.replaceAll(function);
    }
}
