package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface ObjIntConcurrentMap<K> extends ObjIntMap<K>, ConcurrentMapExt<K, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return ObjIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, int)
     */
    @Deprecated
    default Integer putIfAbsent(K key, Integer value) {
        return ObjIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, int)
     */
    @Deprecated
    default Integer replace(K key, Integer value) {
        return ObjIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, int, int)
     */
    @Deprecated
    default boolean replace(K key, Integer oldValue, Integer newValue) {
        return ObjIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Integer computeIfAbsent(K key, @NotNull Function<? super K, ? extends Integer> mappingFunction) {
        return ObjIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        return ObjIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjIntBiFunction)
     */
    @Deprecated
    default Integer compute(K key, @NotNull BiFunction<? super K, ? super Integer, ? extends Integer> remappingFunction) {
        return ObjIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(K key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return ObjIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Integer> action) {
        ObjIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Integer, ? extends Integer> function) {
        ObjIntMap.super.replaceAll(function);
    }
}
