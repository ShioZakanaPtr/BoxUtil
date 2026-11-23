package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortCharBidiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
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
public interface CharShortBidiMap extends BidiMap<Character, Short>, CharShortMap {
    char getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default Character getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    char getKeyOrDefault(short value, char defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, char)
     */
    @Deprecated
    default Character getKeyOrDefault(Object value, Character defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey != null ? defaultKey.charValue() : (char) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, short)
     */
    @Deprecated
    default Short put(Character key, Short value) {
        return CharShortMap.super.put(key, value);
    }

    short putUnique(char key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(char, short)
     */
    @Deprecated
    default Short putUnique(Character key, Short value) {
        return this.putUnique(key != null ? key.charValue() : (char) 0, value != null ? value.shortValue() : (short) 0);
    }

    char removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default Character removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, char)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key != null ? ((Character) key).charValue() : (char) 0);
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

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Character, Short>> entrySet() {
        return CharShortMap.super.entrySet();
    }

    @NotNull ShortCharBidiMap inverse();
}
