package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.longtype.*;
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
public interface LongIntConcurrentMap extends LongIntMap, ConcurrentMapExt<Long, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(long, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return LongIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(long, int)
     */
    @Deprecated
    default Integer putIfAbsent(Long key, Integer value) {
        return LongIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(long, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return LongIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, int)
     */
    @Deprecated
    default Integer replace(Long key, Integer value) {
        return LongIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(long, int, int)
     */
    @Deprecated
    default boolean replace(Long key, Integer oldValue, Integer newValue) {
        return LongIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(long, LongFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Long key, @NotNull Function<? super Long, ? extends Integer> mappingFunction) {
        return LongIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(long, LongIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Long key, @NotNull BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        return LongIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(long, LongIntBiFunction)
     */
    @Deprecated
    default Integer compute(Long key, @NotNull BiFunction<? super Long, ? super Integer, ? extends Integer> remappingFunction) {
        return LongIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(long, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Long key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return LongIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(LongIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Long, ? super Integer> action) {
        LongIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(LongIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Long, ? super Integer, ? extends Integer> function) {
        LongIntMap.super.replaceAll(function);
    }
}
