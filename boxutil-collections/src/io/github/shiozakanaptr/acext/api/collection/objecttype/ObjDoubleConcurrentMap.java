package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface ObjDoubleConcurrentMap<K> extends ObjDoubleMap<K>, ConcurrentMapExt<K, Double> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, double)
     */
    @Deprecated
    default Double getOrDefault(Object key, Double defaultValue) {
        return ObjDoubleMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, double)
     */
    @Deprecated
    default Double putIfAbsent(K key, Double value) {
        return ObjDoubleMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, double)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjDoubleMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, double)
     */
    @Deprecated
    default Double replace(K key, Double value) {
        return ObjDoubleMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, double, double)
     */
    @Deprecated
    default boolean replace(K key, Double oldValue, Double newValue) {
        return ObjDoubleMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Double computeIfAbsent(K key, @NotNull Function<? super K, ? extends Double> mappingFunction) {
        return ObjDoubleMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjDoubleBiFunction)
     */
    @Deprecated
    default Double computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        return ObjDoubleMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjDoubleBiFunction)
     */
    @Deprecated
    default Double compute(K key, @NotNull BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        return ObjDoubleMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, double, DoubleDoubleBiFunction)
     */
    @Deprecated
    default Double merge(K key, @NotNull Double value, @NotNull BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return ObjDoubleMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjDoubleBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Double> action) {
        ObjDoubleMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjDoubleBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Double, ? extends Double> function) {
        ObjDoubleMap.super.replaceAll(function);
    }
}
