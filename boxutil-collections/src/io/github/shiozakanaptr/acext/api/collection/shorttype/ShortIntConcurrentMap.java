package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface ShortIntConcurrentMap extends ShortIntMap, ConcurrentMapExt<Short, Integer> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        return ShortIntMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, int)
     */
    @Deprecated
    default Integer putIfAbsent(Short key, Integer value) {
        return ShortIntMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ShortIntMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, int)
     */
    @Deprecated
    default Integer replace(Short key, Integer value) {
        return ShortIntMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, int, int)
     */
    @Deprecated
    default boolean replace(Short key, Integer oldValue, Integer newValue) {
        return ShortIntMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Integer> mappingFunction) {
        return ShortIntMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        return ShortIntMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortIntBiFunction)
     */
    @Deprecated
    default Integer compute(Short key, @NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> remappingFunction) {
        return ShortIntMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Short key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return ShortIntMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Integer> action) {
        ShortIntMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Integer, ? extends Integer> function) {
        ShortIntMap.super.replaceAll(function);
    }
}
