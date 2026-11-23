package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface DoubleByteConcurrentMap extends DoubleByteMap, ConcurrentMapExt<Double, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return DoubleByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Double key, Byte value) {
        return DoubleByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, byte)
     */
    @Deprecated
    default Byte replace(Double key, Byte value) {
        return DoubleByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, byte, byte)
     */
    @Deprecated
    default boolean replace(Double key, Byte oldValue, Byte newValue) {
        return DoubleByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends Byte> mappingFunction) {
        return DoubleByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
        return DoubleByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleByteBiFunction)
     */
    @Deprecated
    default Byte compute(Double key, @NotNull BiFunction<? super Double, ? super Byte, ? extends Byte> remappingFunction) {
        return DoubleByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Double key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return DoubleByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super Byte> action) {
        DoubleByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super Byte, ? extends Byte> function) {
        DoubleByteMap.super.replaceAll(function);
    }
}
