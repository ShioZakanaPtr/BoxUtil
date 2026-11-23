package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleFloatBidiMap;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
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
public interface FloatDoubleBidiMap extends BidiMap<Float, Double>, FloatDoubleMap {
    float getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Float getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    float getKeyOrDefault(double value, float defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, float)
     */
    @Deprecated
    default Float getKeyOrDefault(Object value, Float defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.floatValue() : (float) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, double)
     */
    @Deprecated
    default Double put(Float key, Double value) {
        return FloatDoubleMap.super.put(key, value);
    }

    double putUnique(float key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(float, double)
     */
    @Deprecated
    default Double putUnique(Float key, Double value) {
        return this.putUnique(key != null ? key.floatValue() : (float) 0, value != null ? value.doubleValue() : (double) 0);
    }

    float removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Float removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, float)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Float) key).floatValue() : (float) 0);
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

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Float, Double>> entrySet() {
        return FloatDoubleMap.super.entrySet();
    }

    @NotNull DoubleFloatBidiMap inverse();
}
