package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface ByteDoubleConcurrentMap extends ByteDoubleMap, ConcurrentMapExt<Byte, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return ByteDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, double)
     */
    @Deprecated
    default Double putIfAbsent(Byte key, Double value) {
        return ByteDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, double)
     */
    @Deprecated
    default Double replace(Byte key, Double value) {
        return ByteDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, double, double)
     */
    @Deprecated
    default boolean replace(Byte key, Double oldValue, Double newValue) {
        return ByteDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Double computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Double> mappingFunction) {
        return ByteDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        return ByteDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteDoubleBiFunction)
     */
    @Deprecated
    default Double compute(Byte key, @NotNull BiFunction<? super Byte, ? super Double, ? extends Double> remappingFunction) {
        return ByteDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(Byte key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return ByteDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Double> action) {
        ByteDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Double, ? extends Double> function) {
        ByteDoubleMap.super.replaceAll(function);
    }
}
