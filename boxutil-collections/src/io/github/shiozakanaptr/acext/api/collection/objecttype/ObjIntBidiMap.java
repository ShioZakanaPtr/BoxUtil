package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntObjBidiMap;
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
public interface ObjIntBidiMap<K> extends BidiMap<K, Integer>, ObjIntMap<K> {
    K getKey(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(int)
     */
    @Deprecated
    default K getKey(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKey();
        return this.getKey(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    K getKeyOrDefault(int value, K defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(int, K)
     */
    @Deprecated
    default K getKeyOrDefault(Object value, K defaultKey) {
        if (value != null && !(value instanceof Integer)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Integer) value).intValue() : (int) 0, defaultKey);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, int)
     */
    @Deprecated
    default Integer put(K key, Integer value) {
        return ObjIntMap.super.put(key, value);
    }

    int putUnique(K key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(K, int)
     */
    @Deprecated
    default Integer putUnique(K key, Integer value) {
        return this.putUnique(key, value != null ? value.intValue() : (int) 0);
    }

    K removeValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int)
     */
    @Deprecated
    default K removeValue(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKey();
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    boolean removeValue(int value, Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int, Object)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (value != null && !(value instanceof Integer)) return false;
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0, key);
    }

    @NotNull IntSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, Integer>> entrySet() {
        return ObjIntMap.super.entrySet();
    }

    @NotNull IntObjBidiMap<K> inverse();
}
