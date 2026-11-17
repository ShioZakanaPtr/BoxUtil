package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface FloatIntConcurrentMap extends FloatIntMap, ConcurrentMapExt<Float, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(float, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return FloatIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(float, int)
     */
    @Deprecated
    default Integer putIfAbsent(Float key, Integer value) {
        return FloatIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(float, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return FloatIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, int)
     */
    @Deprecated
    default Integer replace(Float key, Integer value) {
        return FloatIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(float, int, int)
     */
    @Deprecated
    default boolean replace(Float key, Integer oldValue, Integer newValue) {
        return FloatIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(float, FloatFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Float key, @NotNull Function<? super Float, ? extends Integer> mappingFunction) {
        return FloatIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(float, FloatIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Float key, @NotNull BiFunction<? super Float, ? super Integer, ? extends Integer> remappingFunction) {
        return FloatIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(float, FloatIntBiFunction)
     */
    @Deprecated
    default Integer compute(Float key, @NotNull BiFunction<? super Float, ? super Integer, ? extends Integer> remappingFunction) {
        return FloatIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(float, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Float key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return FloatIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(FloatIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Float, ? super Integer> action) {
        FloatIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(FloatIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Float, ? super Integer, ? extends Integer> function) {
        FloatIntMap.super.replaceAll(function);
    }
}
