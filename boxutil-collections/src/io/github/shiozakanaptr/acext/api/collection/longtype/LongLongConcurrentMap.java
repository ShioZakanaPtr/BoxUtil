package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface LongLongConcurrentMap extends LongLongMap, ConcurrentMapExt<Long, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return LongLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, long)
     */
    @Deprecated
    default Long putIfAbsent(Long key, Long value) {
        return LongLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, long)
     */
    @Deprecated
    default Long replace(Long key, Long value) {
        return LongLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, long, long)
     */
    @Deprecated
    default boolean replace(Long key, Long oldValue, Long newValue) {
        return LongLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Long> mappingFunction) {
        return LongLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return LongLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongLongBiFunction)
     */
    @Deprecated
    default Long compute(Long key, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return LongLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Long key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return LongLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Long> action) {
        LongLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Long, ? extends Long> function) {
        LongLongMap.super.replaceAll(function);
    }
}
