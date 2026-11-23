package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjShortConcurrentMap<K> extends ObjShortMap<K>, ConcurrentMapExt<K, Short> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, short)
     */
    @Deprecated
    default Short getOrDefault(Object key, Short defaultValue) {
        return ObjShortMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, short)
     */
    @Deprecated
    default Short putIfAbsent(K key, Short value) {
        return ObjShortMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, short)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjShortMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, short)
     */
    @Deprecated
    default Short replace(K key, Short value) {
        return ObjShortMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, short, short)
     */
    @Deprecated
    default boolean replace(K key, Short oldValue, Short newValue) {
        return ObjShortMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Short computeIfAbsent(K key, @NotNull Function<? super K, ? extends Short> mappingFunction) {
        return ObjShortMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjShortBiFunction)
     */
    @Deprecated
    default Short computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        return ObjShortMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjShortBiFunction)
     */
    @Deprecated
    default Short compute(K key, @NotNull BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
        return ObjShortMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, short, ShortShortBiFunction)
     */
    @Deprecated
    default Short merge(K key, @NotNull Short value, @NotNull BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
        return ObjShortMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjShortBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Short> action) {
        ObjShortMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjShortBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Short, ? extends Short> function) {
        ObjShortMap.super.replaceAll(function);
    }
}
