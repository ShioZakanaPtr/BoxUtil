package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
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
public interface ByteShortConcurrentMap extends ByteShortMap, ConcurrentMapExt<Byte, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return ByteShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, short)
     */
    @Deprecated
    default Short putIfAbsent(Byte key, Short value) {
        return ByteShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, short)
     */
    @Deprecated
    default Short replace(Byte key, Short value) {
        return ByteShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, short, short)
     */
    @Deprecated
    default boolean replace(Byte key, Short oldValue, Short newValue) {
        return ByteShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Short computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Short> mappingFunction) {
        return ByteShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        return ByteShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteShortBiFunction)
     */
    @Deprecated
    default Short compute(Byte key, @NotNull BiFunction<? super Byte, ? super Short, ? extends Short> remappingFunction) {
        return ByteShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(Byte key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return ByteShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Short> action) {
        ByteShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Short, ? extends Short> function) {
        ByteShortMap.super.replaceAll(function);
    }
}
