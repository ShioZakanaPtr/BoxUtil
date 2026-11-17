package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ByteObjConcurrentMap<V> extends ByteObjMap<V>, ConcurrentMapExt<Byte, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return ByteObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, V)
     */
    @Deprecated
    default V putIfAbsent(Byte key, V value) {
        return ByteObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ByteObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, V)
     */
    @Deprecated
    default V replace(Byte key, V value) {
        return ByteObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, V, V)
     */
    @Deprecated
    default boolean replace(Byte key, V oldValue, V newValue) {
        return ByteObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default V computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends V> mappingFunction) {
        return ByteObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        return ByteObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteObjBiFunction)
     */
    @Deprecated
    default V compute(Byte key, @NotNull BiFunction<? super Byte, ? super V, ? extends V> remappingFunction) {
        return ByteObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, V, BiFunction)
     */
    @Deprecated
    default V merge(Byte key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return ByteObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super V> action) {
        ByteObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super V, ? extends V> function) {
        ByteObjMap.super.replaceAll(function);
    }
}
