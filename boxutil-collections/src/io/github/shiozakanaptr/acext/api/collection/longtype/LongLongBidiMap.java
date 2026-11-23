package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongLongBidiMap;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSet;
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
public interface LongLongBidiMap extends BidiMap<Long, Long>, LongLongMap {
    long getKey(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(long)
     */
    @Deprecated
    default Long getKey(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Long) value).longValue() : (long) 0);
    }

    long getKeyOrDefault(long value, long defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(long, long)
     */
    @Deprecated
    default Long getKeyOrDefault(Object value, Long defaultKey) {
        if (value != null && !(value instanceof Long)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Long) value).longValue() : (long) 0, defaultKey != null ? defaultKey.longValue() : (long) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, long)
     */
    @Deprecated
    default Long put(Long key, Long value) {
        return LongLongMap.super.put(key, value);
    }

    long putUnique(long key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(long, long)
     */
    @Deprecated
    default Long putUnique(Long key, Long value) {
        return this.putUnique(key != null ? key.longValue() : (long) 0, value != null ? value.longValue() : (long) 0);
    }

    long removeValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long)
     */
    @Deprecated
    default Long removeValue(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    boolean removeValue(long value, long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long, long)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0, key != null ? ((Long) key).longValue() : (long) 0);
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

    @NotNull LongSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Long, Long>> entrySet() {
        return LongLongMap.super.entrySet();
    }

    @NotNull LongLongBidiMap inverse();
}
