package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface LongObjConcurrentMap<V> extends LongObjMap<V>, ConcurrentMapExt<Long, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return LongObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, V)
     */
    @Deprecated
    default V putIfAbsent(Long key, V value) {
        return LongObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, V)
     */
    @Deprecated
    default V replace(Long key, V value) {
        return LongObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, V, V)
     */
    @Deprecated
    default boolean replace(Long key, V oldValue, V newValue) {
        return LongObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default V computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends V> mappingFunction) {
        return LongObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        return LongObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongObjBiFunction)
     */
    @Deprecated
    default V compute(Long key, @NotNull BiFunction<? super Long, ? super V, ? extends V> remappingFunction) {
        return LongObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, V, BiFunction)
     */
    @Deprecated
    default V merge(Long key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return LongObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super V> action) {
        LongObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super V, ? extends V> function) {
        LongObjMap.super.replaceAll(function);
    }
}
