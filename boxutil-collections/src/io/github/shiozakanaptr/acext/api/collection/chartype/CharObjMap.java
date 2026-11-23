package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

/**
 * Type specialization interface.
 *
 * @see MapExt
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharObjMap<V> extends MapExt<Character, V> {
    V get(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(char)
     */
    @Deprecated
    default V get(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValue();
        return this.get(key != null ? ((Character) key).charValue() : (char) 0);
    }

    V getOrDefault(char key, V defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, V)
     */
    @Deprecated
    default V getOrDefault(Object key, V defaultValue) {
        if (key != null && !(key instanceof Character)) return defaultValue;
        return this.getOrDefault(key != null ? ((Character) key).charValue() : (char) 0, defaultValue);
    }

    V put(char key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, V)
     */
    @Deprecated
    default V put(Character key, V value) {
        return this.put(key != null ? key.charValue() : (char) 0, value);
    }

    V putIfAbsent(char key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, V)
     */
    @Deprecated
    default V putIfAbsent(Character key, V value) {
        return this.putIfAbsent(key != null ? key.charValue() : (char) 0, value);
    }

    V remove(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char)
     */
    @Deprecated
    default V remove(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValue();
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0);
    }

    boolean remove(char key, Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, Object)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Character)) return false;
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0, value);
    }

    V replace(char key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, V)
     */
    @Deprecated
    default V replace(Character key, V value) {
        return this.replace(key != null ? key.charValue() : (char) 0, value);
    }

    boolean replace(char key, V oldValue, V newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, V, V)
     */
    @Deprecated
    default boolean replace(Character key, V oldValue, V newValue) {
        return this.replace(key != null ? key.charValue() : (char) 0, oldValue, newValue);
    }

    V computeIfAbsent(char key, @NotNull CharFunction<? extends V> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default V computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends V> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.charValue() : (char) 0, (Character l_t) -> mappingFunction.apply(l_t));
    }

    V computeIfPresent(char key, @NotNull CharObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharObjBiFunction)
     */
    @Deprecated
    default V computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        return this.computeIfPresent(key != null ? key.charValue() : (char) 0, (char l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V compute(char key, @NotNull CharObjBiFunction<? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharObjBiFunction)
     */
    @Deprecated
    default V compute(Character key, @NotNull BiFunction<? super Character, ? super V, ? extends V> remappingFunction) {
        return this.compute(key != null ? key.charValue() : (char) 0, (char l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V merge(char key, V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, V, BiFunction)
     */
    @Deprecated
    default V merge(Character key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return this.merge(key != null ? key.charValue() : (char) 0, value, (V l_t, V l_u) -> remappingFunction.apply(l_t, l_u));
    }

    V update(char key, @NotNull Function<? super V, ? extends V> updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(char, Function)
     */
    @Deprecated
    default V update(Character key, @NotNull Function<? super V, ? extends V> updateFunction) {
        return this.update(key != null ? key.charValue() : (char) 0, (V l_t) -> updateFunction.apply(l_t));
    }

    V remapping(char key, @NotNull CharObjBiFunction<? super V, ? extends Character> remappingFunction, BiFunction<? super V, ? super V, ? extends V> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharObjBiFunction, BiFunction)
     */
    @Deprecated
    default V remapping(Character key, @NotNull BiFunction<? super Character, ? super V, ? extends Character> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        return this.remapping(key != null ? key.charValue() : (char) 0, (char l_t, V l_u) -> remappingFunction.apply(l_t, l_u), (V l_t, V l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(char from, char to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(char, char)
     */
    @Deprecated
    default boolean swapAt(Character from, Character to) {
        return this.swapAt(from != null ? from.charValue() : (char) 0, to != null ? to.charValue() : (char) 0);
    }

    boolean containsKey(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(char)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Character)) return false;
        return this.containsKey(key != null ? ((Character) key).charValue() : (char) 0);
    }

    void forEach(@NotNull CharObjBiConsumer<? super V> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharObjBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super V> action) {
        this.forEach((char l_t, V l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull CharObjBiFunction<? super V, ? extends V> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharObjBiFunction)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super V, ? extends V> function) {
        this.replaceAll((char l_t, V l_u) -> function.apply(l_t, l_u));
    }

    @NotNull
    CharSet keySet();

    @NotNull
    ObjCollection<V> values();

    @NotNull
    EntrySetExt<CharObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Character, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<V> extends MapExt.Entry<Character, V> {
        char getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Character getKey() {
            return this.getKeyRaw();
        }
    }

}
