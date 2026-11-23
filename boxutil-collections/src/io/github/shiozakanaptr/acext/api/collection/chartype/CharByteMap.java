package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCollection;
import io.github.shiozakanaptr.acext.util.function.chartype.*;
import io.github.shiozakanaptr.acext.util.function.bytetype.*;
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
public interface CharByteMap extends MapExt<Character, Byte> {
    byte get(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #get(char)
     */
    @Deprecated
    default Byte get(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.get(key != null ? ((Character) key).charValue() : (char) 0);
    }

    byte getOrDefault(char key, byte defaultValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getOrDefault(char, byte)
     */
    @Deprecated
    default Byte getOrDefault(Object key, Byte defaultValue) {
        if (key != null && !(key instanceof Character)) return defaultValue;
        return this.getOrDefault(key != null ? ((Character) key).charValue() : (char) 0, defaultValue != null ? defaultValue.byteValue() : (byte) 0);
    }

    byte put(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, byte)
     */
    @Deprecated
    default Byte put(Character key, Byte value) {
        return this.put(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte putIfAbsent(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putIfAbsent(char, byte)
     */
    @Deprecated
    default Byte putIfAbsent(Character key, Byte value) {
        return this.putIfAbsent(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte remove(char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char)
     */
    @Deprecated
    default Byte remove(Object key) {
        if (key != null && !(key instanceof Character)) return this.getDefaultReturnValueRaw();
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0);
    }

    boolean remove(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remove(char, byte)
     */
    @Deprecated
    default boolean remove(Object key, Object value) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.remove(key != null ? ((Character) key).charValue() : (char) 0, value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte replace(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, byte)
     */
    @Deprecated
    default Byte replace(Character key, Byte value) {
        return this.replace(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0);
    }

    boolean replace(char key, byte oldValue, byte newValue);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replace(char, byte, byte)
     */
    @Deprecated
    default boolean replace(Character key, Byte oldValue, Byte newValue) {
        return this.replace(key != null ? key.charValue() : (char) 0, oldValue != null ? oldValue.byteValue() : (byte) 0, newValue != null ? newValue.byteValue() : (byte) 0);
    }

    byte computeIfAbsent(char key, @NotNull CharFunction<? extends Byte> mappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfAbsent(char, CharFunction)
     */
    @Deprecated
    default Byte computeIfAbsent(Character key, @NotNull Function<? super Character, ? extends Byte> mappingFunction) {
        return this.computeIfAbsent(key != null ? key.charValue() : (char) 0, (Character l_t) -> mappingFunction.apply(l_t));
    }

    byte computeIfPresent(char key, @NotNull CharByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #computeIfPresent(char, CharByteBiFunction)
     */
    @Deprecated
    default Byte computeIfPresent(Character key, @NotNull BiFunction<? super Character, ? super Byte, ? extends Byte> remappingFunction) {
        return this.computeIfPresent(key != null ? key.charValue() : (char) 0, (char l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte compute(char key, @NotNull CharByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #compute(char, CharByteBiFunction)
     */
    @Deprecated
    default Byte compute(Character key, @NotNull BiFunction<? super Character, ? super Byte, ? extends Byte> remappingFunction) {
        return this.compute(key != null ? key.charValue() : (char) 0, (char l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte merge(char key, byte value, @NotNull ByteByteBiFunction<? extends Byte> remappingFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #merge(char, byte, ByteByteBiFunction)
     */
    @Deprecated
    default Byte merge(Character key, @NotNull Byte value, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return this.merge(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0, (byte l_t, byte l_u) -> remappingFunction.apply(l_t, l_u));
    }

    byte update(char key, @NotNull ByteUnaryOperator updateFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #update(char, ByteUnaryOperator)
     */
    @Deprecated
    default Byte update(Character key, @NotNull Function<? super Byte, ? extends Byte> updateFunction) {
        return this.update(key != null ? key.charValue() : (char) 0, (byte l_t) -> {
            final Byte result;
            return (result = updateFunction.apply(l_t)) != null ? result : (byte) 0;
        });
    }

    byte remapping(char key, @NotNull CharByteBiFunction<? extends Character> remappingFunction, ByteByteBiFunction<? extends Byte> mergeFunction);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #remapping(char, CharByteBiFunction, ByteByteBiFunction)
     */
    @Deprecated
    default Byte remapping(Character key, @NotNull BiFunction<? super Character, ? super Byte, ? extends Character> remappingFunction, @NotNull BiFunction<? super Byte, ? super Byte, ? extends Byte> mergeFunction) {
        return this.remapping(key != null ? key.charValue() : (char) 0, (char l_t, byte l_u) -> remappingFunction.apply(l_t, l_u), (byte l_t, byte l_u) -> mergeFunction.apply(l_t, l_u));
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

    boolean containsValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #containsValue(byte)
     */
    @Deprecated
    default boolean containsValue(Object value) {
        if (value != null && !(value instanceof Byte)) return false;
        return this.containsValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    void forEach(@NotNull CharByteBiConsumer action);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #forEach(CharByteBiConsumer)
     */
    @Deprecated
    default void forEach(@NotNull BiConsumer<? super Character, ? super Byte> action) {
        this.forEach((char l_t, byte l_u) -> action.accept(l_t, l_u));
    }

    void replaceAll(@NotNull CharByteBiOperator function);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #replaceAll(CharByteBiOperator)
     */
    @Deprecated
    default void replaceAll(@NotNull BiFunction<? super Character, ? super Byte, ? extends Byte> function) {
        this.replaceAll((char l_t, byte l_u) -> function.apply(l_t, l_u));
    }

    byte getDefaultReturnValueRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnValueRaw()
     */
    @Deprecated
    default Byte getDefaultReturnValue() {
        return this.getDefaultReturnValueRaw();
    }

    byte setDefaultReturnValue(byte newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnValue(byte)
     */
    @Deprecated
    default Byte setDefaultReturnValue(Byte newDefault) {
        return this.setDefaultReturnValue(newDefault != null ? newDefault.byteValue() : (byte) 0);
    }

    @NotNull
    CharSet keySet();

    @NotNull
    ByteCollection values();

    @NotNull
    EntrySetExt<CharByteMap.Entry> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    @NotNull
    default EntrySetExt<MapExt.Entry<Character, Byte>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    interface Entry extends MapExt.Entry<Character, Byte> {
        char getKeyRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getKeyRaw()
         */
        @Deprecated
        default Character getKey() {
            return this.getKeyRaw();
        }

        byte getValueRaw();

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #getValueRaw()
         */
        @Deprecated
        default Byte getValue() {
            return this.getValueRaw();
        }

        byte setValue(byte newValue);

        /**
         * @deprecated Please use the corresponding method that using primitive type.
         * @see #setValue(byte)
         */
        @Deprecated
        default Byte setValue(Byte value) {
            return this.setValue(value != null ? value.byteValue() : (byte) 0);
        }
    }

}
