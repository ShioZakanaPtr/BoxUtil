package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleObjBiMap;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjDoubleBiMap<K> extends BiMap<K, Double>, ObjDoubleMap<K> {
    K getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default K getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKey();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    K getKeyOrDefault(double value, K defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, K)
     */
    @Deprecated
    default K getKeyOrDefault(Object value, K defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, double)
     */
    @Deprecated
    default Double put(K key, Double value) {
        return ObjDoubleMap.super.put(key, value);
    }

    double putUnique(K key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(K, double)
     */
    @Deprecated
    default Double putUnique(K key, Double value) {
        return this.putUnique(key, value != null ? value.doubleValue() : (double) 0);
    }

    K removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default K removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKey();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, Object)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key);
    }

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, Double>> entrySet() {
        return ObjDoubleMap.super.entrySet();
    }

    @NotNull DoubleObjBiMap<K> inverse();
}
