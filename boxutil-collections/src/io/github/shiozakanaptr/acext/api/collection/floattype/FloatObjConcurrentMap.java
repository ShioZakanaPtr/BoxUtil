package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FloatObjConcurrentMap<V> extends FloatObjMap<V>, ConcurrentMapExt<Float, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return FloatObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, V)
     */
    @Deprecated
    default V putIfAbsent(Float key, V value) {
        return FloatObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, V)
     */
    @Deprecated
    default V replace(Float key, V value) {
        return FloatObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, V, V)
     */
    @Deprecated
    default boolean replace(Float key, V oldValue, V newValue) {
        return FloatObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default V computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends V> mappingFunction) {
        return FloatObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
        return FloatObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatObjBiFunction)
     */
    @Deprecated
    default V compute(Float key, @NotNull BiFunction<? super Float, ? super V, ? extends V> remappingFunction) {
        return FloatObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, V, BiFunction)
     */
    @Deprecated
    default V merge(Float key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return FloatObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super V> action) {
        FloatObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super V, ? extends V> function) {
        FloatObjMap.super.replaceAll(function);
    }
}
