package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.util.function.objecttype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
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
public interface ObjCharMap<K> extends MapExt<K, Character> {
    char getRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getRaw(Object)
     */
    @Deprecated
    default Character get(Object key) {
        return this.getRaw(key);
    }

    char getOrDefault(Object key, char defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(Object, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        return this.getOrDefault(key, defaultValue != null ? defaultValue.charValue() : (char) 0);
    }

    char put(K key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, char)
     */
    @Deprecated
    default Character put(K key, Character value) {
        return this.put(key, value != null ? value.charValue() : (char) 0);
    }

    char putIfAbsent(K key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(K, char)
     */
    @Deprecated
    default Character putIfAbsent(K key, Character value) {
        return this.putIfAbsent(key, value != null ? value.charValue() : (char) 0);
    }

    char removeRaw(Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeRaw(Object)
     */
    @Deprecated
    default Character remove(Object key) {
        return this.removeRaw(key);
    }

    boolean remove(Object key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(Object, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (value != null && !(value instanceof Character)) return false;
        return this.remove(key, value != null ? ((Character) value).charValue() : (char) 0);
    }

    char replace(K key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, char)
     */
    @Deprecated
    default Character replace(K key, Character value) {
        return this.replace(key, value != null ? value.charValue() : (char) 0);
    }

    boolean replace(K key, char oldValue, char newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(K, char, char)
     */
    @Deprecated
    default boolean replace(K key, Character oldValue, Character newValue) {
        return this.replace(key, oldValue != null ? oldValue.charValue() : (char) 0, newValue != null ? newValue.charValue() : (char) 0);
    }

    char computeIfAbsentRaw(K key, @NotNull Function<? super K, ? extends Character> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsentRaw(K, Function)
     */
    @Deprecated
    default Character computeIfAbsent(K key, @NotNull Function<? super K, ? extends Character> mappingFunction) {
        return this.computeIfAbsentRaw(key, (K l_t) -> mappingFunction.apply(l_t));
    }

    char computeIfPresent(K key, @NotNull ObjCharBiFunction<? super K, ? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(K, ObjCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(K key, @NotNull BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        return this.computeIfPresent(key, (K l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char compute(K key, @NotNull ObjCharBiFunction<? super K, ? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(K, ObjCharBiFunction)
     */
    @Deprecated
    default Character compute(K key, @NotNull BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        return this.compute(key, (K l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char merge(K key, char value, @NotNull CharCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(K, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(K key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return this.merge(key, value != null ? value.charValue() : (char) 0, (char l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char update(K key, @NotNull CharUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(K, CharUnaryOperator)
     */
    @Deprecated
    default Character update(K key, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(key, (char l_t) -> {
            final Character result;
            return (result = updateFunction.apply(l_t)) != null ? result : (char) 0;
        });
    }

    char remapping(K key, @NotNull ObjCharBiFunction<? super K, ? extends K> remappingFunction, CharCharBiFunction<? extends Character> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(K, ObjCharBiFunction, CharCharBiFunction)
     */
    @Deprecated
    default Character remapping(K key, @NotNull BiFunction<? super K, ? super Character, ? extends K> remappingFunction, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> mergeFunction) {
        return this.remapping(key, (K l_t, char l_u) -> remappingFunction.apply(l_t, l_u), (char l_t, char l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean containsValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(char)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Character)) return false;
        return this.containsValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    void forEach(@NotNull ObjCharBiConsumer<? super K> action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ObjCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super K, ? super Character> action) {
        this.forEach((K l_t, char l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ObjCharBiOperator<? super K> function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ObjCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super K, ? super Character, ? extends Character> function) {
        this.replaceAll((K l_t, char l_u) -> function.apply(l_t, l_u));
    }

    char getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Character getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    char setDefaultReturnValue(char newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(char)
     */
    @Deprecated
    default Character setDefaultReturnValue(Character newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.charValue() : (char) 0);
    }

    @NotNull
    ObjSet<K> keySet();

    @NotNull
    CharCollection values();

    @NotNull
    EntrySetExt<ObjCharMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<K, Character>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry<K> extends MapExt.Entry<K, Character> {

        char getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Character getValue() {
            return this.getValueRaw();
        }

        char setValue(char newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(char)
         */
        @Deprecated
        default Character setValue(Character value) {
            return this.setValue(value != null ? value.charValue() : (char) 0);
        }
    }

}
