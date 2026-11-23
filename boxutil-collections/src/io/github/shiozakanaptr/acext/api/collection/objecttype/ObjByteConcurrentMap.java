package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjByteConcurrentMap<K> extends ObjByteMap<K>, ConcurrentMapExt<K, Byte> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        return ObjByteMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, byte)
     */
    @Deprecated
    default Byte putIfAbsent(K key, Byte value) {
        return ObjByteMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjByteMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, byte)
     */
    @Deprecated
    default Byte replace(K key, Byte value) {
        return ObjByteMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, byte, byte)
     */
    @Deprecated
    default boolean replace(K key, Byte oldValue, Byte newValue) {
        return ObjByteMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Byte computeIfAbsent(K key, @NotNull Function<? super K, ? extends Byte> mappingFunction) {
        return ObjByteMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        return ObjByteMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjByteBiFunction)
     */
    @Deprecated
    default Byte compute(K key, @NotNull BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        return ObjByteMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(K key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return ObjByteMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Byte> action) {
        ObjByteMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Byte, ? extends Byte> function) {
        ObjByteMap.super.replaceAll(function);
    }
}
