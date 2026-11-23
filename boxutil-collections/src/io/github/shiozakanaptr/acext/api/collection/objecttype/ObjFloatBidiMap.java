package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.floattype.FloatObjBidiMap;
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
public interface ObjFloatBidiMap<K> extends BidiMap<K, Float>, ObjFloatMap<K> {
    K getKey(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(float)
     */
    @Deprecated
    default K getKey(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKey();
        return this.getKey(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    K getKeyOrDefault(float value, K defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(float, K)
     */
    @Deprecated
    default K getKeyOrDefault(Object value, K defaultKey) {
        if (value != null && !(value instanceof Float)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Float) value).floatValue() : (float) 0, defaultKey);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, float)
     */
    @Deprecated
    default Float put(K key, Float value) {
        return ObjFloatMap.super.put(key, value);
    }

    float putUnique(K key, float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(K, float)
     */
    @Deprecated
    default Float putUnique(K key, Float value) {
        return this.putUnique(key, value != null ? value.floatValue() : (float) 0);
    }

    K removeValue(float value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float)
     */
    @Deprecated
    default K removeValue(Object value) {
        if (value != null && !(value instanceof Float)) return this.getDefaultReturnKey();
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0);
    }

    boolean removeValue(float value, Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(float, Object)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (value != null && !(value instanceof Float)) return false;
        return this.removeValue(value != null ? ((Float) value).floatValue() : (float) 0, key);
    }

    @NotNull FloatSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, Float>> entrySet() {
        return ObjFloatMap.super.entrySet();
    }

    @NotNull FloatObjBidiMap<K> inverse();
}
