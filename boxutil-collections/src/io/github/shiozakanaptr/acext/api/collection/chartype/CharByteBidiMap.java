package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteCharBidiMap;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Type specialization interface.
 *
 * @see BidiMap
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface CharByteBidiMap extends BidiMap<Character, Byte>, CharByteMap {
    char getKey(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(byte)
     */
    @Deprecated
    default Character getKey(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    char getKeyOrDefault(byte value, char defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(byte, char)
     */
    @Deprecated
    default Character getKeyOrDefault(Object value, Character defaultKey) {
        if (value != null && !(value instanceof Byte)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Byte) value).byteValue() : (byte) 0, defaultKey != null ? defaultKey.charValue() : (char) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, byte)
     */
    @Deprecated
    default Byte put(Character key, Byte value) {
        return CharByteMap.super.put(key, value);
    }

    byte putUnique(char key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(char, byte)
     */
    @Deprecated
    default Byte putUnique(Character key, Byte value) {
        return this.putUnique(key != null ? key.charValue() : (char) 0, value != null ? value.byteValue() : (byte) 0);
    }

    char removeValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte)
     */
    @Deprecated
    default Character removeValue(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    boolean removeValue(byte value, char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte, char)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0, key != null ? ((Character) key).charValue() : (char) 0);
    }

    char getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Character getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    char setDefaultReturnKey(char newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(char)
     */
    @Deprecated
    default Character setDefaultReturnKey(@Nullable Character newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.charValue() : (char) 0);
    }

    @NotNull ByteSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Character, Byte>> entrySet() {
        return CharByteMap.super.entrySet();
    }

    @NotNull ByteCharBidiMap inverse();
}
