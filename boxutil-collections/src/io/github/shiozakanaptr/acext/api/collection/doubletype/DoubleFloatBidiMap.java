package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatDoubleBidiMap;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatSet;
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
public interface DoubleFloatBidiMap extends BidiMap<Double, Float>, DoubleFloatMap {
    double getKey(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(float)
     */
    @Deprecated
    default Double getKey(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    double getKeyOrDefault(float value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(float, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        if (value != null && !(value instanceof Float)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Float) value).floatValue() : (float) 0, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, float)
     */
    @Deprecated
    default Float put(Double key, Float value) {
        return DoubleFloatMap.super.put(key, value);
    }

    float putUnique(double key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, float)
     */
    @Deprecated
    default Float putUnique(Double key, Float value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value != null ? value.floatValue() : (float) 0);
    }

    double removeValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float)
     */
    @Deprecated
    default Double removeValue(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    boolean removeValue(float value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Float)) return false;
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0, key != null ? ((Double) key).doubleValue() : (double) 0);
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

    @NotNull FloatSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, Float>> entrySet() {
        return DoubleFloatMap.super.entrySet();
    }

    @NotNull FloatDoubleBidiMap inverse();
}
