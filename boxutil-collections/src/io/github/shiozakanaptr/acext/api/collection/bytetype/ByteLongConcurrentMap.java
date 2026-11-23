package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface ByteLongConcurrentMap extends ByteLongMap, ConcurrentMapExt<Byte, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return ByteLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, long)
     */
    @Deprecated
    default Long putIfAbsent(Byte key, Long value) {
        return ByteLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, long)
     */
    @Deprecated
    default Long replace(Byte key, Long value) {
        return ByteLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, long, long)
     */
    @Deprecated
    default boolean replace(Byte key, Long oldValue, Long newValue) {
        return ByteLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Long> mappingFunction) {
        return ByteLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
        return ByteLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteLongBiFunction)
     */
    @Deprecated
    default Long compute(Byte key, @NotNull BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
        return ByteLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Byte key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return ByteLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Long> action) {
        ByteLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Long, ? extends Long> function) {
        ByteLongMap.super.replaceAll(function);
    }
}
