package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharIntBidiMap;
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
public interface IntCharBidiMap extends BidiMap<Integer, Character>, IntCharMap {
    int getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default Integer getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    int getKeyOrDefault(char value, int defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, int)
     */
    @Deprecated
    default Integer getKeyOrDefault(Object value, Integer defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey != null ? defaultKey.intValue() : (int) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, char)
     */
    @Deprecated
    default Character put(Integer key, Character value) {
        return IntCharMap.super.put(key, value);
    }

    char putUnique(int key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(int, char)
     */
    @Deprecated
    default Character putUnique(Integer key, Character value) {
        return this.putUnique(key != null ? key.intValue() : (int) 0, value != null ? value.charValue() : (char) 0);
    }

    int removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default Integer removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, int)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key != null ? ((Integer) key).intValue() : (int) 0);
    }

    int getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Integer getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    int setDefaultReturnKey(int newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(int)
     */
    @Deprecated
    default Integer setDefaultReturnKey(@Nullable Integer newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Integer, Character>> entrySet() {
        return IntCharMap.super.entrySet();
    }

    @NotNull CharIntBidiMap inverse();
}
