package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatCharBiMap;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CharFloatBiMap extends BiMap<Character, Float>, CharFloatMap {
    char getKey(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(float)
     */
    @Deprecated
    default Character getKey(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    char getKeyOrDefault(float value, char defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(float, char)
     */
    @Deprecated
    default Character getKeyOrDefault(Object value, Character defaultKey) {
        if (value != null && !(value instanceof Float)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Float) value).floatValue() : (float) 0, defaultKey != null ? defaultKey.charValue() : (char) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, float)
     */
    @Deprecated
    default Float put(Character key, Float value) {
        return CharFloatMap.super.put(key, value);
    }

    float putUnique(char key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(char, float)
     */
    @Deprecated
    default Float putUnique(Character key, Float value) {
        return this.putUnique(key != null ? key.charValue() : (char) 0, value != null ? value.floatValue() : (float) 0);
    }

    char removeValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float)
     */
    @Deprecated
    default Character removeValue(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    boolean removeValue(float value, char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float, char)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0, key != null ? ((Character) key).charValue() : (char) 0);
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

    @NotNull FloatSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Character, Float>> entrySet() {
        return CharFloatMap.super.entrySet();
    }

    @NotNull FloatCharBiMap inverse();
}
