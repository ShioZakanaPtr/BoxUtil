package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongShortBidiMap;
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
public interface ShortLongBidiMap extends BidiMap<Short, Long>, ShortLongMap {
    short getKey(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(long)
     */
    @Deprecated
    default Short getKey(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Long) value).longValue() : (long) 0);
    }

    short getKeyOrDefault(long value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(long, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        if (value != null && !(value instanceof Long)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Long) value).longValue() : (long) 0, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, long)
     */
    @Deprecated
    default Long put(Short key, Long value) {
        return ShortLongMap.super.put(key, value);
    }

    long putUnique(short key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, long)
     */
    @Deprecated
    default Long putUnique(Short key, Long value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value != null ? value.longValue() : (long) 0);
    }

    short removeValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long)
     */
    @Deprecated
    default Short removeValue(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    boolean removeValue(long value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0, key != null ? ((Short) key).shortValue() : (short) 0);
    }

    short getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Short getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    short setDefaultReturnKey(short newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(short)
     */
    @Deprecated
    default Short setDefaultReturnKey(@Nullable Short newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.shortValue() : (short) 0);
    }

    @NotNull LongSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, Long>> entrySet() {
        return ShortLongMap.super.entrySet();
    }

    @NotNull LongShortBidiMap inverse();
}
