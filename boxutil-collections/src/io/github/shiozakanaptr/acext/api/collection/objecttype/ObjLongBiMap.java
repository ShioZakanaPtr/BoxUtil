package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongObjBiMap;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSet;
import org.jetbrains.annotations.NotNull;

public interface ObjLongBiMap<K> extends BiMap<K, Long>, ObjLongMap<K> {
    K getKey(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(long)
     */
    @Deprecated
    default K getKey(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKey();
        return this.getKey(value != null ? ((Long) value).longValue() : (long) 0);
    }

    K getKeyOrDefault(long value, K defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(long, K)
     */
    @Deprecated
    default K getKeyOrDefault(Object value, K defaultKey) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKey();
        return this.getKeyOrDefault(value != null ? ((Long) value).longValue() : (long) 0, defaultKey);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, long)
     */
    @Deprecated
    default Long put(K key, Long value) {
        return ObjLongMap.super.put(key, value);
    }

    long putUnique(K key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(K, long)
     */
    @Deprecated
    default Long putUnique(K key, Long value) {
        return this.putUnique(key, value != null ? value.longValue() : (long) 0);
    }

    K removeValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long)
     */
    @Deprecated
    default K removeValue(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKey();
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    boolean removeValue(long value, Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long, Object)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (value != null && !(value instanceof Long)) return false;
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0, key);
    }

    @NotNull LongSet values();

    @NotNull EntrySetExt<ObjLongMap.Entry<K>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, Long>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    @NotNull LongObjBiMap<K> inverse();
}
