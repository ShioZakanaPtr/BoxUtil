package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatIntBiMap;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IntFloatBiMap extends BiMap<Integer, Float>, IntFloatMap {
    int getKey(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(float)
     */
    @Deprecated
    default Integer getKey(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    int getKeyOrDefault(float value, int defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(float, int)
     */
    @Deprecated
    default Integer getKeyOrDefault(Object value, Integer defaultKey) {
        if (value != null && !(value instanceof Float)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Float) value).floatValue() : (float) 0, defaultKey != null ? defaultKey.intValue() : (int) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, float)
     */
    @Deprecated
    default Float put(Integer key, Float value) {
        return IntFloatMap.super.put(key, value);
    }

    float putUnique(int key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(int, float)
     */
    @Deprecated
    default Float putUnique(Integer key, Float value) {
        return this.putUnique(key != null ? key.intValue() : (int) 0, value != null ? value.floatValue() : (float) 0);
    }

    int removeValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float)
     */
    @Deprecated
    default Integer removeValue(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    boolean removeValue(float value, int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float, int)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0, key != null ? ((Integer) key).intValue() : (int) 0);
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

    @NotNull FloatSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Integer, Float>> entrySet() {
        return IntFloatMap.super.entrySet();
    }

    @NotNull FloatIntBiMap inverse();
}
