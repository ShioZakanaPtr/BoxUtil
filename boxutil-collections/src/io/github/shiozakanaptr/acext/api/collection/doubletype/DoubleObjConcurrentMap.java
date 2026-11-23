package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.doubletype.*;
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
public interface DoubleObjConcurrentMap<V> extends DoubleObjMap<V>, ConcurrentMapExt<Double, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(double, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return DoubleObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(double, V)
     */
    @Deprecated
    default V putIfAbsent(Double key, V value) {
        return DoubleObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(double, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return DoubleObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, V)
     */
    @Deprecated
    default V replace(Double key, V value) {
        return DoubleObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(double, V, V)
     */
    @Deprecated
    default boolean replace(Double key, V oldValue, V newValue) {
        return DoubleObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(double, DoubleFunction)
     */
    @Deprecated
    default V computeIfAbsent(Double key, @NotNull Function<? super Double, ? extends V> mappingFunction) {
        return DoubleObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(double, DoubleObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Double key, @NotNull BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        return DoubleObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(double, DoubleObjBiFunction)
     */
    @Deprecated
    default V compute(Double key, @NotNull BiFunction<? super Double, ? super V, ? extends V> remappingFunction) {
        return DoubleObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(double, V, BiFunction)
     */
    @Deprecated
    default V merge(Double key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return DoubleObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(DoubleObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Double, ? super V> action) {
        DoubleObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(DoubleObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Double, ? super V, ? extends V> function) {
        DoubleObjMap.super.replaceAll(function);
    }
}
