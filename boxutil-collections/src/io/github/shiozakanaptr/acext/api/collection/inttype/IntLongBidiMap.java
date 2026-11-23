package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongIntBidiMap;
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
public interface IntLongBidiMap extends BidiMap<Integer, Long>, IntLongMap {
    int getKey(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(long)
     */
    @Deprecated
    default Integer getKey(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Long) value).longValue() : (long) 0);
    }

    int getKeyOrDefault(long value, int defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(long, int)
     */
    @Deprecated
    default Integer getKeyOrDefault(Object value, Integer defaultKey) {
        if (value != null && !(value instanceof Long)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Long) value).longValue() : (long) 0, defaultKey != null ? defaultKey.intValue() : (int) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, long)
     */
    @Deprecated
    default Long put(Integer key, Long value) {
        return IntLongMap.super.put(key, value);
    }

    long putUnique(int key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(int, long)
     */
    @Deprecated
    default Long putUnique(Integer key, Long value) {
        return this.putUnique(key != null ? key.intValue() : (int) 0, value != null ? value.longValue() : (long) 0);
    }

    int removeValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long)
     */
    @Deprecated
    default Integer removeValue(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    boolean removeValue(long value, int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long, int)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0, key != null ? ((Integer) key).intValue() : (int) 0);
    }

    int getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Integer getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    int setDefaultReturnKey(int newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(int)
     */
    @Deprecated
    default Integer setDefaultReturnKey(@Nullable Integer newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.intValue() : (int) 0);
    }

    @NotNull LongSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Integer, Long>> entrySet() {
        return IntLongMap.super.entrySet();
    }

    @NotNull LongIntBidiMap inverse();
}
