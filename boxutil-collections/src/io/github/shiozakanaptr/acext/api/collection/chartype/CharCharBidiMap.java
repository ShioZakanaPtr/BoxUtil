package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharCharBidiMap;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharSet;
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
public interface CharCharBidiMap extends BidiMap<Character, Character>, CharCharMap {
    char getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default Character getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    char getKeyOrDefault(char value, char defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, char)
     */
    @Deprecated
    default Character getKeyOrDefault(Object value, Character defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey != null ? defaultKey.charValue() : (char) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, char)
     */
    @Deprecated
    default Character put(Character key, Character value) {
        return CharCharMap.super.put(key, value);
    }

    char putUnique(char key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(char, char)
     */
    @Deprecated
    default Character putUnique(Character key, Character value) {
        return this.putUnique(key != null ? key.charValue() : (char) 0, value != null ? value.charValue() : (char) 0);
    }

    char removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default Character removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, char)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key != null ? ((Character) key).charValue() : (char) 0);
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

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Character, Character>> entrySet() {
        return CharCharMap.super.entrySet();
    }

    @NotNull CharCharBidiMap inverse();
}
