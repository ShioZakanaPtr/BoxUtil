package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface IntObjConcurrentMap<V> extends IntObjMap<V>, ConcurrentMapExt<Integer, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return IntObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, V)
     */
    @Deprecated
    default V putIfAbsent(Integer key, V value) {
        return IntObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, V)
     */
    @Deprecated
    default V replace(Integer key, V value) {
        return IntObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, V, V)
     */
    @Deprecated
    default boolean replace(Integer key, V oldValue, V newValue) {
        return IntObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default V computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends V> mappingFunction) {
        return IntObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        return IntObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntObjBiFunction)
     */
    @Deprecated
    default V compute(Integer key, @NotNull BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        return IntObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, V, BiFunction)
     */
    @Deprecated
    default V merge(Integer key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return IntObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super V> action) {
        IntObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super V, ? extends V> function) {
        IntObjMap.super.replaceAll(function);
    }
}
