package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleLongBiMap;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LongDoubleBiMap extends BiMap<Long, Double>, LongDoubleMap {
    long getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Long getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    long getKeyOrDefault(double value, long defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, long)
     */
    @Deprecated
    default Long getKeyOrDefault(Object value, Long defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.longValue() : (long) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, double)
     */
    @Deprecated
    default Double put(Long key, Double value) {
        return LongDoubleMap.super.put(key, value);
    }

    double putUnique(long key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(long, double)
     */
    @Deprecated
    default Double putUnique(Long key, Double value) {
        return this.putUnique(key != null ? key.longValue() : (long) 0, value != null ? value.doubleValue() : (double) 0);
    }

    long removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Long removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, long)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Long) key).longValue() : (long) 0);
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

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Long, Double>> entrySet() {
        return LongDoubleMap.super.entrySet();
    }

    @NotNull DoubleLongBiMap inverse();
}
