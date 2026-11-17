package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntLongConcurrentMap extends IntLongMap, ConcurrentMapExt<Integer, Long> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, long)
     */
    @Deprecated
    default Long getOrDefault(Object key, Long defaultValue) {
        return IntLongMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, long)
     */
    @Deprecated
    default Long putIfAbsent(Integer key, Long value) {
        return IntLongMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, long)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntLongMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, long)
     */
    @Deprecated
    default Long replace(Integer key, Long value) {
        return IntLongMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, long, long)
     */
    @Deprecated
    default boolean replace(Integer key, Long oldValue, Long newValue) {
        return IntLongMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Long computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Long> mappingFunction) {
        return IntLongMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntLongBiFunction)
     */
    @Deprecated
    default Long computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
        return IntLongMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntLongBiFunction)
     */
    @Deprecated
    default Long compute(Integer key, @NotNull BiFunction<? super Integer, ? super Long, ? extends Long> remappingFunction) {
        return IntLongMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, long, LongLongBiFunction)
     */
    @Deprecated
    default Long merge(Integer key, @NotNull Long value, @NotNull BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
        return IntLongMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntLongBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Long> action) {
        IntLongMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntLongBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Long, ? extends Long> function) {
        IntLongMap.super.replaceAll(function);
    }
}
