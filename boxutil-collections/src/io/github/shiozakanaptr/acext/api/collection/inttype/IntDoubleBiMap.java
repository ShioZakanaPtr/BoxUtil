package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleIntBiMap;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IntDoubleBiMap extends BiMap<Integer, Double>, IntDoubleMap {
    int getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Integer getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    int getKeyOrDefault(double value, int defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, int)
     */
    @Deprecated
    default Integer getKeyOrDefault(Object value, Integer defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.intValue() : (int) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, double)
     */
    @Deprecated
    default Double put(Integer key, Double value) {
        return IntDoubleMap.super.put(key, value);
    }

    double putUnique(int key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(int, double)
     */
    @Deprecated
    default Double putUnique(Integer key, Double value) {
        return this.putUnique(key != null ? key.intValue() : (int) 0, value != null ? value.doubleValue() : (double) 0);
    }

    int removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Integer removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, int)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Integer) key).intValue() : (int) 0);
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

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Integer, Double>> entrySet() {
        return IntDoubleMap.super.entrySet();
    }

    @NotNull DoubleIntBiMap inverse();
}
