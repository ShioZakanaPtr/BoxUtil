package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.inttype.*;
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
public interface CharIntMap extends MapExt<Character, Integer> {
    int get(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(char)
     */
    @Deprecated
    default Integer get(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Character) key).charValue() : (char) 0);
    }

    int getOrDefault(char key, int defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, int)
     */
    @Deprecated
    default Integer getOrDefault(Object key, Integer defaultValue) {
        if (key != null && !(key instanceof Character)) return defaultValue;
        return this.getOrDefault(key != null ? ((Character) key).charValue() : (char) 0, defaultValue != null ? defaultValue.intValue() : (int) 0);
    }

    int put(char key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, int)
     */
    @Deprecated
    default Integer put(Character key, Integer value) {
        return this.put(key != null ? key.charValue() : (char) 0, value != null ? value.intValue() : (int) 0);
    }

    int putIfAbsent(char key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, int)
     */
    @Deprecated
    default Integer putIfAbsent(Character key, Integer value) {
        return this.putIfAbsent(key != null ? key.charValue() : (char) 0, value != null ? value.intValue() : (int) 0);
    }

    int remove(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char)
     */
    @Deprecated
    default Integer remove(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0);
    }

    boolean remove(char key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, int)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0, value != null ? ((Integer) value).intValue() : (int) 0);
    }

    int replace(char key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, int)
     */
    @Deprecated
    default Integer replace(Character key, Integer value) {
        return this.replace(key != null ? key.charValue() : (char) 0, value != null ? value.intValue() : (int) 0);
    }

    boolean replace(char key, int oldValue, int newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, int, int)
     */
    @Deprecated
    default boolean replace(Character key, Integer oldValue, Integer newValue) {
        return this.replace(key != null ? key.charValue() : (char) 0, oldValue != null ? oldValue.intValue() : (int) 0, newValue != null ? newValue.intValue() : (int) 0);
    }

    int computeIfAbsent(char key, @NotNull CharFunction<? extends Integer> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Integer computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Integer> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.charValue() : (char) 0, (Character l_t) -> mappingFunction.apply(l_t));
    }

    int computeIfPresent(char key, @NotNull CharIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharIntBiFunction)
     */
    @Deprecated
    default Integer computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Integer, ? extends Integer> remappingFunction) {
        return this.computeIfPresent(key != null ? key.charValue() : (char) 0, (char l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int compute(char key, @NotNull CharIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharIntBiFunction)
     */
    @Deprecated
    default Integer compute(Character key, @NotNull BiFunction<? super Character, ? super Integer, ? extends Integer> remappingFunction) {
        return this.compute(key != null ? key.charValue() : (char) 0, (char l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int merge(char key, int value, @NotNull IntIntBiFunction<? extends Integer> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, int, IntIntBiFunction)
     */
    @Deprecated
    default Integer merge(Character key, @NotNull Integer value, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return this.merge(key != null ? key.charValue() : (char) 0, value.intValue(), (int l_t, int l_u) -> remappingFunction.apply(l_t, l_u));
    }

    int update(char key, @NotNull IntUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(char, IntUnaryOperator)
     */
    @Deprecated
    default Integer update(Character key, @NotNull Function<? super Integer, ? extends Integer> updateFunction) {
        return this.update(key != null ? key.charValue() : (char) 0, (int l_t) -> {
            final Integer result;
            return (result = updateFunction.apply(l_t)) != null ? result : (int) 0;
        });
    }

    int remapping(char key, @NotNull CharIntBiFunction<? extends Character> remappingFunction, IntIntBiFunction<? extends Integer> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharIntBiFunction, IntIntBiFunction)
     */
    @Deprecated
    default Integer remapping(Character key, @NotNull BiFunction<? super Character, ? super Integer, ? extends Character> remappingFunction, @NotNull BiFunction<? super Integer, ? super Integer, ? extends Integer> mergeFunction) {
        return this.remapping(key != null ? key.charValue() : (char) 0, (char l_t, int l_u) -> remappingFunction.apply(l_t, l_u), (int l_t, int l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(int)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.containsValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    void forEach(@NotNull CharIntBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharIntBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Integer> action) {
        this.forEach((char l_t, int l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull CharIntBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharIntBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Integer, ? extends Integer> function) {
        this.replaceAll((char l_t, int l_u) -> function.apply(l_t, l_u));
    }

    int getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Integer getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    int setDefaultReturnValue(int newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(int)
     */
    @Deprecated
    default Integer setDefaultReturnValue(Integer newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull
    CharSet keySet();

    @NotNull
    IntCollection values();

    @NotNull
    EntrySetExt<CharIntMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Character, Integer>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Character, Integer> {
        char getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Character getKey() {
            return this.getKeyRaw();
        }

        int getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Integer getValue() {
            return this.getValueRaw();
        }

        int setValue(int newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(int)
         */
        @Deprecated
        default Integer setValue(Integer value) {
            return this.setValue(value != null ? value.intValue() : (int) 0);
        }
    }

}
