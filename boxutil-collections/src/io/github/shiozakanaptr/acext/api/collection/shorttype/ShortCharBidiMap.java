package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharShortBidiMap;
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
public interface ShortCharBidiMap extends BidiMap<Short, Character>, ShortCharMap {
    short getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default Short getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    short getKeyOrDefault(char value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, char)
     */
    @Deprecated
    default Character put(Short key, Character value) {
        return ShortCharMap.super.put(key, value);
    }

    char putUnique(short key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, char)
     */
    @Deprecated
    default Character putUnique(Short key, Character value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value != null ? value.charValue() : (char) 0);
    }

    short removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default Short removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key != null ? ((Short) key).shortValue() : (short) 0);
    }

    short getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Short getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    short setDefaultReturnKey(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(short)
     */
    @Deprecated
    default Short setDefaultReturnKey(@Nullable Short newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, Character>> entrySet() {
        return ShortCharMap.super.entrySet();
    }

    @NotNull CharShortBidiMap inverse();
}
