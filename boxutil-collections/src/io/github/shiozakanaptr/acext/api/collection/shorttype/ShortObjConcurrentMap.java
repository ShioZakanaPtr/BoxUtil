package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ShortObjConcurrentMap<V> extends ShortObjMap<V>, ConcurrentMapExt<Short, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return ShortObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, V)
     */
    @Deprecated
    default V putIfAbsent(Short key, V value) {
        return ShortObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, V)
     */
    @Deprecated
    default V replace(Short key, V value) {
        return ShortObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, V, V)
     */
    @Deprecated
    default boolean replace(Short key, V oldValue, V newValue) {
        return ShortObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default V computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends V> mappingFunction) {
        return ShortObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        return ShortObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortObjBiFunction)
     */
    @Deprecated
    default V compute(Short key, @NotNull BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
        return ShortObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, V, BiFunction)
     */
    @Deprecated
    default V merge(Short key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return ShortObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super V> action) {
        ShortObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super V, ? extends V> function) {
        ShortObjMap.super.replaceAll(function);
    }
}
