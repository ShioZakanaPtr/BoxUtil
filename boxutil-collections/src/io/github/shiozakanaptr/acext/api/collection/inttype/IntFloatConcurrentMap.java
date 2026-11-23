package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
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
public interface IntFloatConcurrentMap extends IntFloatMap, ConcurrentMapExt<Integer, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return IntFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, float)
     */
    @Deprecated
    default Float putIfAbsent(Integer key, Float value) {
        return IntFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, float)
     */
    @Deprecated
    default Float replace(Integer key, Float value) {
        return IntFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, float, float)
     */
    @Deprecated
    default boolean replace(Integer key, Float oldValue, Float newValue) {
        return IntFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Float> mappingFunction) {
        return IntFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
        return IntFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntFloatBiFunction)
     */
    @Deprecated
    default Float compute(Integer key, @NotNull BiFunction<? super Integer, ? super Float, ? extends Float> remappingFunction) {
        return IntFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Integer key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return IntFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Float> action) {
        IntFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Float, ? extends Float> function) {
        IntFloatMap.super.replaceAll(function);
    }
}
