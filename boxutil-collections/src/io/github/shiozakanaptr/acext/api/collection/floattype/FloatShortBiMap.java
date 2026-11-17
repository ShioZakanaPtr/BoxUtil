package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortFloatBiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FloatShortBiMap extends BiMap<Float, Short>, FloatShortMap {
    float getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default Float getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    float getKeyOrDefault(short value, float defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, float)
     */
    @Deprecated
    default Float getKeyOrDefault(Object value, Float defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey != null ? defaultKey.floatValue() : (float) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, short)
     */
    @Deprecated
    default Short put(Float key, Short value) {
        return FloatShortMap.super.put(key, value);
    }

    short putUnique(float key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(float, short)
     */
    @Deprecated
    default Short putUnique(Float key, Short value) {
        return this.putUnique(key != null ? key.floatValue() : (float) 0, value != null ? value.shortValue() : (short) 0);
    }

    float removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default Float removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, float)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key != null ? ((Float) key).floatValue() : (float) 0);
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

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Float, Short>> entrySet() {
        return FloatShortMap.super.entrySet();
    }

    @NotNull ShortFloatBiMap inverse();
}
