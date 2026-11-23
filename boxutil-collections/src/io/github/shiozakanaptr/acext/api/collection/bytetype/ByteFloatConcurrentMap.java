package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface ByteFloatConcurrentMap extends ByteFloatMap, ConcurrentMapExt<Byte, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return ByteFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, float)
     */
    @Deprecated
    default Float putIfAbsent(Byte key, Float value) {
        return ByteFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, float)
     */
    @Deprecated
    default Float replace(Byte key, Float value) {
        return ByteFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, float, float)
     */
    @Deprecated
    default boolean replace(Byte key, Float oldValue, Float newValue) {
        return ByteFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Float> mappingFunction) {
        return ByteFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
        return ByteFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteFloatBiFunction)
     */
    @Deprecated
    default Float compute(Byte key, @NotNull BiFunction<? super Byte, ? super Float, ? extends Float> remappingFunction) {
        return ByteFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Byte key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return ByteFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Float> action) {
        ByteFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Float, ? extends Float> function) {
        ByteFloatMap.super.replaceAll(function);
    }
}
