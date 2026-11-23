package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface IntCharMap extends MapExt<Integer, Character> {
    char get(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(int)
     */
    @Deprecated
    default Character get(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    char getOrDefault(int key, char defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(int, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key != null && !(key instanceof Integer)) return defaultValue;
        return this.getOrDefault(key != null ? ((Integer) key).intValue() : (int) 0, defaultValue != null ? defaultValue.charValue() : (char) 0);
    }

    char put(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, char)
     */
    @Deprecated
    default Character put(Integer key, Character value) {
        return this.put(key != null ? key.intValue() : (int) 0, value != null ? value.charValue() : (char) 0);
    }

    char putIfAbsent(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(int, char)
     */
    @Deprecated
    default Character putIfAbsent(Integer key, Character value) {
        return this.putIfAbsent(key != null ? key.intValue() : (int) 0, value != null ? value.charValue() : (char) 0);
    }

    char remove(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int)
     */
    @Deprecated
    default Character remove(Object key) {
        if (key != null && !(key instanceof Integer)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0);
    }

    boolean remove(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(int, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.remove(key != null ? ((Integer) key).intValue() : (int) 0, value != null ? ((Character) value).charValue() : (char) 0);
    }

    char replace(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, char)
     */
    @Deprecated
    default Character replace(Integer key, Character value) {
        return this.replace(key != null ? key.intValue() : (int) 0, value != null ? value.charValue() : (char) 0);
    }

    boolean replace(int key, char oldValue, char newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(int, char, char)
     */
    @Deprecated
    default boolean replace(Integer key, Character oldValue, Character newValue) {
        return this.replace(key != null ? key.intValue() : (int) 0, oldValue != null ? oldValue.charValue() : (char) 0, newValue != null ? newValue.charValue() : (char) 0);
    }

    char computeIfAbsent(int key, @NotNull IntFunction<? extends Character> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(int, IntFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Integer key, @NotNull Function<? super Integer, ? extends Character> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.intValue() : (int) 0, (Integer l_t) -> mappingFunction.apply(l_t));
    }

    char computeIfPresent(int key, @NotNull IntCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(int, IntCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Integer key, @NotNull BiFunction<? super Integer, ? super Character, ? extends Character> remappingFunction) {
        return this.computeIfPresent(key != null ? key.intValue() : (int) 0, (int l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char compute(int key, @NotNull IntCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(int, IntCharBiFunction)
     */
    @Deprecated
    default Character compute(Integer key, @NotNull BiFunction<? super Integer, ? super Character, ? extends Character> remappingFunction) {
        return this.compute(key != null ? key.intValue() : (int) 0, (int l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char merge(int key, char value, @NotNull CharCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(int, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Integer key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return this.merge(key != null ? key.intValue() : (int) 0, value.charValue(), (char l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char update(int key, @NotNull CharUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(int, CharUnaryOperator)
     */
    @Deprecated
    default Character update(Integer key, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(key != null ? key.intValue() : (int) 0, (char l_t) -> {
            final Character result;
            return (result = updateFunction.apply(l_t)) != null ? result : (char) 0;
        });
    }

    char remapping(int key, @NotNull IntCharBiFunction<? extends Integer> remappingFunction, CharCharBiFunction<? extends Character> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(int, IntCharBiFunction, CharCharBiFunction)
     */
    @Deprecated
    default Character remapping(Integer key, @NotNull BiFunction<? super Integer, ? super Character, ? extends Integer> remappingFunction, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> mergeFunction) {
        return this.remapping(key != null ? key.intValue() : (int) 0, (int l_t, char l_u) -> remappingFunction.apply(l_t, l_u), (char l_t, char l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(int from, int to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(int, int)
     */
    @Deprecated
    default boolean swapAt(Integer from, Integer to) {
        return this.swapAt(from != null ? from.intValue() : (int) 0, to != null ? to.intValue() : (int) 0);
    }

    boolean containsKey(int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(int)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        return this.containsKey(key != null ? ((Integer) key).intValue() : (int) 0);
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

    void forEach(@NotNull IntCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(IntCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Integer, ? super Character> action) {
        this.forEach((int l_t, char l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull IntCharBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(IntCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Integer, ? super Character, ? extends Character> function) {
        this.replaceAll((int l_t, char l_u) -> function.apply(l_t, l_u));
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
    IntSet keySet();

    @NotNull
    CharCollection values();

    @NotNull
    EntrySetExt<IntCharMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Integer, Character>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Integer, Character> {
        int getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Integer getKey() {
            return this.getKeyRaw();
        }

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
