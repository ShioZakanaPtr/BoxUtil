package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCollection;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
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
public interface ByteCharMap extends MapExt<Byte, Character> {
    char get(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(byte)
     */
    @Deprecated
    default Character get(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    char getOrDefault(byte key, char defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(byte, char)
     */
    @Deprecated
    default Character getOrDefault(Object key, Character defaultValue) {
        if (key != null && !(key instanceof Byte)) return defaultValue;
        return this.getOrDefault(key != null ? ((Byte) key).byteValue() : (byte) 0, defaultValue != null ? defaultValue.charValue() : (char) 0);
    }

    char put(byte key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, char)
     */
    @Deprecated
    default Character put(Byte key, Character value) {
        return this.put(key != null ? key.byteValue() : (byte) 0, value != null ? value.charValue() : (char) 0);
    }

    char putIfAbsent(byte key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(byte, char)
     */
    @Deprecated
    default Character putIfAbsent(Byte key, Character value) {
        return this.putIfAbsent(key != null ? key.byteValue() : (byte) 0, value != null ? value.charValue() : (char) 0);
    }

    char remove(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte)
     */
    @Deprecated
    default Character remove(Object key) {
        if (key != null && !(key instanceof Byte)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    boolean remove(byte key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(byte, char)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.remove(key != null ? ((Byte) key).byteValue() : (byte) 0, value != null ? ((Character) value).charValue() : (char) 0);
    }

    char replace(byte key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, char)
     */
    @Deprecated
    default Character replace(Byte key, Character value) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, value != null ? value.charValue() : (char) 0);
    }

    boolean replace(byte key, char oldValue, char newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(byte, char, char)
     */
    @Deprecated
    default boolean replace(Byte key, Character oldValue, Character newValue) {
        return this.replace(key != null ? key.byteValue() : (byte) 0, oldValue != null ? oldValue.charValue() : (char) 0, newValue != null ? newValue.charValue() : (char) 0);
    }

    char computeIfAbsent(byte key, @NotNull ByteFunction<? extends Character> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(byte, ByteFunction)
     */
    @Deprecated
    default Character computeIfAbsent(Byte key, @NotNull Function<? super Byte, ? extends Character> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.byteValue() : (byte) 0, (Byte l_t) -> mappingFunction.apply(l_t));
    }

    char computeIfPresent(byte key, @NotNull ByteCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(byte, ByteCharBiFunction)
     */
    @Deprecated
    default Character computeIfPresent(Byte key, @NotNull BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
        return this.computeIfPresent(key != null ? key.byteValue() : (byte) 0, (byte l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char compute(byte key, @NotNull ByteCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(byte, ByteCharBiFunction)
     */
    @Deprecated
    default Character compute(Byte key, @NotNull BiFunction<? super Byte, ? super Character, ? extends Character> remappingFunction) {
        return this.compute(key != null ? key.byteValue() : (byte) 0, (byte l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char merge(byte key, char value, @NotNull CharCharBiFunction<? extends Character> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(byte, char, CharCharBiFunction)
     */
    @Deprecated
    default Character merge(Byte key, @NotNull Character value, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return this.merge(key != null ? key.byteValue() : (byte) 0, value != null ? value.charValue() : (char) 0, (char l_t, char l_u) -> remappingFunction.apply(l_t, l_u));
    }

    char update(byte key, @NotNull CharUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(byte, CharUnaryOperator)
     */
    @Deprecated
    default Character update(Byte key, @NotNull Function<? super Character, ? extends Character> updateFunction) {
        return this.update(key != null ? key.byteValue() : (byte) 0, (char l_t) -> {
            final Character result;
            return (result = updateFunction.apply(l_t)) != null ? result : (char) 0;
        });
    }

    char remapping(byte key, @NotNull ByteCharBiFunction<? extends Byte> remappingFunction, CharCharBiFunction<? extends Character> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(byte, ByteCharBiFunction, CharCharBiFunction)
     */
    @Deprecated
    default Character remapping(Byte key, @NotNull BiFunction<? super Byte, ? super Character, ? extends Byte> remappingFunction, @NotNull BiFunction<? super Character, ? super Character, ? extends Character> mergeFunction) {
        return this.remapping(key != null ? key.byteValue() : (byte) 0, (byte l_t, char l_u) -> remappingFunction.apply(l_t, l_u), (char l_t, char l_u) -> mergeFunction.apply(l_t, l_u));
    }

    boolean swapAt(byte from, byte to);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #swapAt(byte, byte)
     */
    @Deprecated
    default boolean swapAt(Byte from, Byte to) {
        return this.swapAt(from != null ? from.byteValue() : (byte) 0, to != null ? to.byteValue() : (byte) 0);
    }

    boolean containsKey(byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsKey(byte)
     */
    @Deprecated
    default boolean containsKey(Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.containsKey(key != null ? ((Byte) key).byteValue() : (byte) 0);
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

    void forEach(@NotNull ByteCharBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(ByteCharBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Byte, ? super Character> action) {
        this.forEach((byte l_t, char l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull ByteCharBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(ByteCharBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Byte, ? super Character, ? extends Character> function) {
        this.replaceAll((byte l_t, char l_u) -> function.apply(l_t, l_u));
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
    ByteSet keySet();

    @NotNull
    CharCollection values();

    @NotNull
    EntrySetExt<ByteCharMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Byte, Character>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Byte, Character> {
        byte getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Byte getKey() {
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
