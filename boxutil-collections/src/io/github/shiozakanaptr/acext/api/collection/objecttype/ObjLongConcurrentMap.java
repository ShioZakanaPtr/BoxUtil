package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
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
public interface ObjLongConcurrentMap<K> extends ObjLongMap<K>, ConcurrentMapExt<K, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return ObjLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, long)
     */
    @Deprecated
    default Long putIfAbsent(K key, Long value) {
        return ObjLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, long)
     */
    @Deprecated
    default Long replace(K key, Long value) {
        return ObjLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, long, long)
     */
    @Deprecated
    default boolean replace(K key, Long oldValue, Long newValue) {
        return ObjLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Long computeIfAbsent(K key, @NotNull Function<? super K, ? extends Long> mappingFunction) {
        return ObjLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        return ObjLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjLongBiFunction)
     */
    @Deprecated
    default Long compute(K key, @NotNull BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
        return ObjLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(K key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return ObjLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Long> action) {
        ObjLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Long, ? extends Long> function) {
        ObjLongMap.super.replaceAll(function);
    }
}
