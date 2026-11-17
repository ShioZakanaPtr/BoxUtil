package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleDoubleBiMap;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DoubleDoubleBiMap extends BiMap<Double, Double>, DoubleDoubleMap {
    double getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Double getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    double getKeyOrDefault(double value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, double)
     */
    @Deprecated
    default Double put(Double key, Double value) {
        return DoubleDoubleMap.super.put(key, value);
    }

    double putUnique(double key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, double)
     */
    @Deprecated
    default Double putUnique(Double key, Double value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value != null ? value.doubleValue() : (double) 0);
    }

    double removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Double removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Double) key).doubleValue() : (double) 0);
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

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, Double>> entrySet() {
        return DoubleDoubleMap.super.entrySet();
    }

    @NotNull DoubleDoubleBiMap inverse();
}
