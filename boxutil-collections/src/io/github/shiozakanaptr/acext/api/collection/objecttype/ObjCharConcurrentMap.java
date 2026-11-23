package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.ConcurrentMapExt;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface ObjCharConcurrentMap<K> extends ObjCharMap<K>, ConcurrentMapExt<K, Character> {
    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return ObjCharMap.super.getOrDefault(key, defaultValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, char)
     */
    @Deprecated
    default Character putIfAbsent(K key, Character value) {
        return ObjCharMap.super.putIfAbsent(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        return ObjCharMap.super.remove(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, char)
     */
    @Deprecated
    default Character replace(K key, Character value) {
        return ObjCharMap.super.replace(key, value);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, char, char)
     */
    @Deprecated
    default boolean replace(K key, Character oldValue, Character newValue) {
        return ObjCharMap.super.replace(key, oldValue, newValue);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Character computeIfAbsent(K key, @NotNull Function<? super K, ? extends Character> mappingFunction) {
        return ObjCharMap.super.computeIfAbsent(key, mappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        return ObjCharMap.super.computeIfPresent(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjCharBiFunction)
     */
    @Deprecated
    default Character compute(K key, @NotNull BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        return ObjCharMap.super.compute(key, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(K key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return ObjCharMap.super.merge(key, value, remappingFunction);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Character> action) {
        ObjCharMap.super.forEach(action);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Character, ? extends Character> function) {
        ObjCharMap.super.replaceAll(function);
    }
}
