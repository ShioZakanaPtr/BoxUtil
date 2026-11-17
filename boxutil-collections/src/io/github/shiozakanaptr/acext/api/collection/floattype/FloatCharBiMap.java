package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharFloatBiMap;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FloatCharBiMap extends BiMap<Float, Character>, FloatCharMap {
    float getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default Float getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    float getKeyOrDefault(char value, float defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, float)
     */
    @Deprecated
    default Float getKeyOrDefault(Object value, Float defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey != null ? defaultKey.floatValue() : (float) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, char)
     */
    @Deprecated
    default Character put(Float key, Character value) {
        return FloatCharMap.super.put(key, value);
    }

    char putUnique(float key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(float, char)
     */
    @Deprecated
    default Character putUnique(Float key, Character value) {
        return this.putUnique(key != null ? key.floatValue() : (float) 0, value != null ? value.charValue() : (char) 0);
    }

    float removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default Float removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, float)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key != null ? ((Float) key).floatValue() : (float) 0);
    }

    float getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Float getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    float setDefaultReturnKey(float newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(float)
     */
    @Deprecated
    default Float setDefaultReturnKey(@Nullable Float newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.floatValue() : (float) 0);
    }

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Float, Character>> entrySet() {
        return FloatCharMap.super.entrySet();
    }

    @NotNull CharFloatBiMap inverse();
}
