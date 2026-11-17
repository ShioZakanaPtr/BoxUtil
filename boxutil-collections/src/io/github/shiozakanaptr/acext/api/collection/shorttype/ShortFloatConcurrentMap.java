package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.floattype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ShortFloatConcurrentMap extends ShortFloatMap, ConcurrentMapExt<Short, Float> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, float)
     */
    @Deprecated
    default Float getOrDefault(Object key, Float defaultValue) {
        return ShortFloatMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, float)
     */
    @Deprecated
    default Float putIfAbsent(Short key, Float value) {
        return ShortFloatMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, float)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortFloatMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, float)
     */
    @Deprecated
    default Float replace(Short key, Float value) {
        return ShortFloatMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, float, float)
     */
    @Deprecated
    default boolean replace(Short key, Float oldValue, Float newValue) {
        return ShortFloatMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Float computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Float> mappingFunction) {
        return ShortFloatMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortFloatBiFunction)
     */
    @Deprecated
    default Float computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Float, ? extends Float> remappingFunction) {
        return ShortFloatMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortFloatBiFunction)
     */
    @Deprecated
    default Float compute(Short key, @NotNull BiFunction<? super Short, ? super Float, ? extends Float> remappingFunction) {
        return ShortFloatMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, float, FloatFloatBiFunction)
     */
    @Deprecated
    default Float merge(Short key, @NotNull Float value, @NotNull BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
        return ShortFloatMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortFloatBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Float> action) {
        ShortFloatMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortFloatBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Float, ? extends Float> function) {
        ShortFloatMap.super.replaceAll(function);
    }
}
