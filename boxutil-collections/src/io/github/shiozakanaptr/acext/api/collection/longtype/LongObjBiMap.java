package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjLongBiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LongObjBiMap<V> extends BiMap<Long, V>, LongObjMap<V> {
    long getKeyRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw(Object)
     */
    default Long getKey(Object value) {
        return this.getKeyRaw(value);
    }

    long getKeyOrDefault(Object value, long defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(Object, long)
     */
    default Long getKeyOrDefault(Object value, Long defaultKey) {
        return this.getKeyOrDefault(value, defaultKey != null ? defaultKey.longValue() : (long) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, V)
     */
    default V put(Long key, V value) {
        return LongObjMap.super.put(key, value);
    }

    V putUnique(long key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(long, V)
     */
    default V putUnique(Long key, V value) {
        return this.putUnique(key != null ? key.longValue() : (long) 0, value);
    }

    long removeValueRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValueRaw(Object)
     */
    default Long removeValue(Object value) {
        return this.removeValueRaw(value);
    }

    boolean removeValue(Object value, long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(Object, long)
     */
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Long)) return false;
        return this.removeValue(value, key != null ? ((Long) key).longValue() : (long) 0);
    }

    long getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Long getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    long setDefaultReturnKey(long newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(long)
     */
    @Deprecated
    default Long setDefaultReturnKey(@Nullable Long newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.longValue() : (long) 0);
    }

    @NotNull ObjSet<V> values();

    @NotNull EntrySetExt<LongObjMap.Entry<V>> entrySetRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Long, V>> entrySet() {
        return (EntrySetExt) this.entrySetRaw();
    }

    @NotNull ObjLongBiMap<V> inverse();
}
