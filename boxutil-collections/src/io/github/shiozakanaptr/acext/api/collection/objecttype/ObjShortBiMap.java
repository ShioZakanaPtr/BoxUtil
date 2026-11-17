package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortObjBiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjShortBiMap<K> extends BiMap<K, Short>, ObjShortMap<K> {
    K getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default K getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKey();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    K getKeyOrDefault(short value, K defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, K)
     */
    @Deprecated
    default K getKeyOrDefault(Object value, K defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, short)
     */
    @Deprecated
    default Short put(K key, Short value) {
        return ObjShortMap.super.put(key, value);
    }

    short putUnique(K key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(K, short)
     */
    @Deprecated
    default Short putUnique(K key, Short value) {
        return this.putUnique(key, value != null ? value.shortValue() : (short) 0);
    }

    K removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default K removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKey();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, Object)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key);
    }

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, Short>> entrySet() {
        return ObjShortMap.super.entrySet();
    }

    @NotNull ShortObjBiMap<K> inverse();
}
