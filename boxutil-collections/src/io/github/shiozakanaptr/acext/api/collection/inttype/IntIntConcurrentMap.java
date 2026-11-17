package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface IntIntConcurrentMap extends IntIntMap, ConcurrentMapExt<Integer, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return IntIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, int)
     */
    @Deprecated
    default Integer putIfAbsent(Integer key, Integer value) {
        return IntIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return IntIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int)
     */
    @Deprecated
    default Integer replace(Integer key, Integer value) {
        return IntIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, int, int)
     */
    @Deprecated
    default boolean replace(Integer key, Integer oldValue, Integer newValue) {
        return IntIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Integer> mappingFunction) {
        return IntIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return IntIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntIntBiFunction)
     */
    @Deprecated
    default Integer compute(Integer key, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return IntIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Integer key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return IntIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Integer> action) {
        IntIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> function) {
        IntIntMap.super.replaceAll(function);
    }
}
