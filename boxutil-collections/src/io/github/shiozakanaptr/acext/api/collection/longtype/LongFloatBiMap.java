package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatLongBiMap;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LongFloatBiMap extends BiMap<Long, Float>, LongFloatMap {
    long getKey(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(float)
     */
    @Deprecated
    default Long getKey(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    long getKeyOrDefault(float value, long defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(float, long)
     */
    @Deprecated
    default Long getKeyOrDefault(Object value, Long defaultKey) {
        if (value != null && !(value instanceof Float)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Float) value).floatValue() : (float) 0, defaultKey != null ? defaultKey.longValue() : (long) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, float)
     */
    @Deprecated
    default Float put(Long key, Float value) {
        return LongFloatMap.super.put(key, value);
    }

    float putUnique(long key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(long, float)
     */
    @Deprecated
    default Float putUnique(Long key, Float value) {
        return this.putUnique(key != null ? key.longValue() : (long) 0, value != null ? value.floatValue() : (float) 0);
    }

    long removeValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float)
     */
    @Deprecated
    default Long removeValue(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    boolean removeValue(float value, long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float, long)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0, key != null ? ((Long) key).longValue() : (long) 0);
    }

    long getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Long getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    long setDefaultReturnKey(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(long)
     */
    @Deprecated
    default Long setDefaultReturnKey(@Nullable Long newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull FloatSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Long, Float>> entrySet() {
        return LongFloatMap.super.entrySet();
    }

    @NotNull FloatLongBiMap inverse();
}
