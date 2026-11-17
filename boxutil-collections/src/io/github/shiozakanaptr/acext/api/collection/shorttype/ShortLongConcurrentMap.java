package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ShortLongConcurrentMap extends ShortLongMap, ConcurrentMapExt<Short, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return ShortLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, long)
     */
    @Deprecated
    default Long putIfAbsent(Short key, Long value) {
        return ShortLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, long)
     */
    @Deprecated
    default Long replace(Short key, Long value) {
        return ShortLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, long, long)
     */
    @Deprecated
    default boolean replace(Short key, Long oldValue, Long newValue) {
        return ShortLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Long> mappingFunction) {
        return ShortLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        return ShortLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortLongBiFunction)
     */
    @Deprecated
    default Long compute(Short key, @NotNull BiFunction<? super Short, ? super Long, ? extends Long> remappingFunction) {
        return ShortLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Short key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return ShortLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Long> action) {
        ShortLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Long, ? extends Long> function) {
        ShortLongMap.super.replaceAll(function);
    }
}
