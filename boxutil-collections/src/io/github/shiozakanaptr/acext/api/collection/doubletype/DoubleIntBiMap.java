package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntDoubleBiMap;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DoubleIntBiMap extends BiMap<Double, Integer>, DoubleIntMap {
    double getKey(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(int)
     */
    @Deprecated
    default Double getKey(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    double getKeyOrDefault(int value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(int, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        if (value != null && !(value instanceof Integer)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Integer) value).intValue() : (int) 0, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, int)
     */
    @Deprecated
    default Integer put(Double key, Integer value) {
        return DoubleIntMap.super.put(key, value);
    }

    int putUnique(double key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, int)
     */
    @Deprecated
    default Integer putUnique(Double key, Integer value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value != null ? value.intValue() : (int) 0);
    }

    double removeValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int)
     */
    @Deprecated
    default Double removeValue(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    boolean removeValue(int value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0, key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    double getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Double getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    double setDefaultReturnKey(double newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(double)
     */
    @Deprecated
    default Double setDefaultReturnKey(@Nullable Double newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.doubleValue() : (double) 0);
    }

    @NotNull IntSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, Integer>> entrySet() {
        return DoubleIntMap.super.entrySet();
    }

    @NotNull IntDoubleBiMap inverse();
}
