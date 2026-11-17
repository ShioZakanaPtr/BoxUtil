package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface CharObjConcurrentMap<V> extends CharObjMap<V>, ConcurrentMapExt<Character, V> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        return CharObjMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, V)
     */
    @Deprecated
    default V putIfAbsent(Character key, V value) {
        return CharObjMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return CharObjMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, V)
     */
    @Deprecated
    default V replace(Character key, V value) {
        return CharObjMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, V, V)
     */
    @Deprecated
    default boolean replace(Character key, V oldValue, V newValue) {
        return CharObjMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default V computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends V> mappingFunction) {
        return CharObjMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        return CharObjMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharObjBiFunction)
     */
    @Deprecated
    default V compute(Character key, @NotNull BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        return CharObjMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, V, BiFunction)
     */
    @Deprecated
    default V merge(Character key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return CharObjMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super V> action) {
        CharObjMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super V, ? extends V> function) {
        CharObjMap.super.replaceAll(function);
    }
}
