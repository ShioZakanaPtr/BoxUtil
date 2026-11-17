package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.util.function.shorttype.*;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;

public interface ShortCharMap extends MapExt<Short, Character> {
    char get(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(short)
     */
    @Deprecated
    default Character get(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    char getOrDefault(short key, char defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(short, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key != null && !(key instanceof Short)) return defaultValue;
        return this.getOrDefault(key != null ? ((Short) key).shortValue() : (short) 0, defaultValue != null ? defaultValue.charValue() : (char) 0);
    }

    char put(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, char)
     */
    @Deprecated
    default Character put(Short key, Character value) {
        return this.put(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0);
    }

    char putIfAbsent(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(short, char)
     */
    @Deprecated
    default Character putIfAbsent(Short key, Character value) {
        return this.putIfAbsent(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0);
    }

    char remove(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short)
     */
    @Deprecated
    default Character remove(Object key) {
        if (key != null && !(key instanceof Short)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0);
    }

    boolean remove(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(short, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.remove(key != null ? ((Short) key).shortValue() : (short) 0, value != null ? ((Character) value).charValue() : (char) 0);
    }

    char replace(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, char)
     */
    @Deprecated
    default Character replace(Short key, Character value) {
        return this.replace(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0);
    }

    boolean replace(short key, char oldValue, char newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(short, char, char)
     */
    @Deprecated
    default boolean replace(Short key, Character oldValue, Character newValue) {
        return this.replace(key != null ? key.shortValue() : (short) 0, oldValue != null ? oldValue.charValue() : (char) 0, newValue != null ? newValue.charValue() : (char) 0);
    }

    char computeIfAbsent(short key, @NotNull ShortFunction<? extends Character> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(short, ShortFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Short key, @NotNull Function<? super Short, ? extends Character> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.shortValue() : (short) 0, (Short l_t) -> mappingFunction.apply(l_t));
    }

    char computeIfPresent(short key, @NotNull ShortCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(short, ShortCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Short key, @NotNull BiFunction<? super Short, ? super Character, ? extends Character> remappingFunction) {
        return this.computeIfPresent(key != null ? key.shortValue() : (short) 0, (short l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char compute(short key, @NotNull ShortCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(short, ShortCharBiFunction)
     */
    @Deprecated
    default Character compute(Short key, @NotNull BiFunction<? super Short, ? super Character, ? extends Character> remappingFunction) {
        return this.compute(key != null ? key.shortValue() : (short) 0, (short l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char merge(short key, char value, @NotNull CharCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(short, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Short key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return this.merge(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0, (char l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char update(short key, @NotNull CharUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(short, CharUnaryOperator)
     */
    @Deprecated
    default Character update(Short key, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(key != null ? key.shortValue() : (short) 0, (char l_t) -> {
            final Character result;
            return (result = updateFunction.apply(l_t)) != null ? result : (char) 0;
        });
    }

    char remapping(short key, @NotNull ShortCharBiFunction<? extends Short> remappingFunction, CharCharBiFunction<? extends Character> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(short, ShortCharBiFunction, CharCharBiFunction)
     */
    @Deprecated
    default Character remapping(Short key, @NotNull BiFunction<? super Short, ? super Character, ? extends Short> remappingFunction, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> mergeFunction) {
        return this.remapping(key != null ? key.shortValue() : (short) 0, (short l_t, char l_u) -> remappingFunction.apply(l_t, l_u), (char l_t, char l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(short from, short to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(short, short)
     */
    @Deprecated
    default boolean swapAt(Short from, Short to) {
        return this.swapAt(from != null ? from.shortValue() : (short) 0, to != null ? to.shortValue() : (short) 0);
    }

    boolean containsKey(short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(short)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Short)) return false;
        return this.containsKey(key != null ? ((Short) key).shortValue() : (short) 0);
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

    void forEach(@NotNull ShortCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ShortCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Short, ? super Character> action) {
        this.forEach((short l_t, char l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ShortCharBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ShortCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Short, ? super Character, ? extends Character> function) {
        this.replaceAll((short l_t, char l_u) -> function.apply(l_t, l_u));
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
    ShortSet keySet();

    @NotNull
    CharCollection values();

    @NotNull
    EntrySetExt<ShortCharMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Short, Character>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Short, Character> {
        short getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Short getKey() {
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
