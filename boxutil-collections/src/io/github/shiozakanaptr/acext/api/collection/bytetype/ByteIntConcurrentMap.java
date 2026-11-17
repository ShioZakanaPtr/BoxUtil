package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ByteIntConcurrentMap extends ByteIntMap, ConcurrentMapExt<Byte, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return ByteIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, int)
     */
    @Deprecated
    default Integer putIfAbsent(Byte key, Integer value) {
        return ByteIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, int)
     */
    @Deprecated
    default Integer replace(Byte key, Integer value) {
        return ByteIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, int, int)
     */
    @Deprecated
    default boolean replace(Byte key, Integer oldValue, Integer newValue) {
        return ByteIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Integer> mappingFunction) {
        return ByteIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> remappingFunction) {
        return ByteIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteIntBiFunction)
     */
    @Deprecated
    default Integer compute(Byte key, @NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> remappingFunction) {
        return ByteIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Byte key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return ByteIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Integer> action) {
        ByteIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Integer, ? extends Integer> function) {
        ByteIntMap.super.replaceAll(function);
    }
}
