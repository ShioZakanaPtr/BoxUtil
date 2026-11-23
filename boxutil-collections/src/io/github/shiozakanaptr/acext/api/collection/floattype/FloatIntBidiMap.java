package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntFloatBidiMap;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSet;
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
public interface FloatIntBidiMap extends BidiMap<Float, Integer>, FloatIntMap {
    float getKey(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(int)
     */
    @Deprecated
    default Float getKey(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    float getKeyOrDefault(int value, float defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(int, float)
     */
    @Deprecated
    default Float getKeyOrDefault(Object value, Float defaultKey) {
        if (value != null && !(value instanceof Integer)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Integer) value).intValue() : (int) 0, defaultKey != null ? defaultKey.floatValue() : (float) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, int)
     */
    @Deprecated
    default Integer put(Float key, Integer value) {
        return FloatIntMap.super.put(key, value);
    }

    int putUnique(float key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(float, int)
     */
    @Deprecated
    default Integer putUnique(Float key, Integer value) {
        return this.putUnique(key != null ? key.floatValue() : (float) 0, value != null ? value.intValue() : (int) 0);
    }

    float removeValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int)
     */
    @Deprecated
    default Float removeValue(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    boolean removeValue(int value, float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int, float)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Float)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0, key != null ? ((Float) key).floatValue() : (float) 0);
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

    @NotNull IntSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Float, Integer>> entrySet() {
        return FloatIntMap.super.entrySet();
    }

    @NotNull IntFloatBidiMap inverse();
}
