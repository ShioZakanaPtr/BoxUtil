package io.github.shiozakanaptr.acext.api.collection.floattype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjFloatBiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface FloatObjBiMap<V> extends BiMap<Float, V>, FloatObjMap<V> {
    float getKeyRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw(Object)
     */
    @Deprecated
    default Float getKey(Object value) {
        return this.getKeyRaw(value);
    }

    float getKeyOrDefault(Object value, float defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(Object, float)
     */
    @Deprecated
    default Float getKeyOrDefault(Object value, Float defaultKey) {
        return this.getKeyOrDefault(value, defaultKey != null ? defaultKey.floatValue() : (float) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(float, V)
     */
    @Deprecated
    default V put(Float key, V value) {
        return FloatObjMap.super.put(key, value);
    }

    V putUnique(float key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(float, V)
     */
    @Deprecated
    default V putUnique(Float key, V value) {
        return this.putUnique(key != null ? key.floatValue() : (float) 0, value);
    }

    float removeValueRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValueRaw(Object)
     */
    @Deprecated
    default Float removeValue(Object value) {
        return this.removeValueRaw(value);
    }

    boolean removeValue(Object value, float key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(Object, float)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Float)) return false;
        return this.removeValue(value, key != null ? ((Float) key).floatValue() : (float) 0);
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

    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Float, V>> entrySet() {
        return FloatObjMap.super.entrySet();
    }

    @NotNull ObjFloatBiMap<V> inverse();
}
