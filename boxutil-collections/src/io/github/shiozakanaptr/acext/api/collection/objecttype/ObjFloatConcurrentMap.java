package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ObjFloatConcurrentMap<K> extends ObjFloatMap<K>, ConcurrentMapExt<K, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return ObjFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, float)
     */
    @Deprecated
    default Float putIfAbsent(K key, Float value) {
        return ObjFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, float)
     */
    @Deprecated
    default Float replace(K key, Float value) {
        return ObjFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, float, float)
     */
    @Deprecated
    default boolean replace(K key, Float oldValue, Float newValue) {
        return ObjFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Float computeIfAbsent(K key, @NotNull Function<? super K, ? extends Float> mappingFunction) {
        return ObjFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        return ObjFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjFloatBiFunction)
     */
    @Deprecated
    default Float compute(K key, @NotNull BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
        return ObjFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(K key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return ObjFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Float> action) {
        ObjFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Float, ? extends Float> function) {
        ObjFloatMap.super.replaceAll(function);
    }
}
