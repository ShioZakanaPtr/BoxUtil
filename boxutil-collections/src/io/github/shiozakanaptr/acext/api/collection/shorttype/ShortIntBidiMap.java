package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntShortBidiMap;
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
public interface ShortIntBidiMap extends BidiMap<Short, Integer>, ShortIntMap {
    short getKey(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(int)
     */
    @Deprecated
    default Short getKey(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    short getKeyOrDefault(int value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(int, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        if (value != null && !(value instanceof Integer)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Integer) value).intValue() : (int) 0, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, int)
     */
    @Deprecated
    default Integer put(Short key, Integer value) {
        return ShortIntMap.super.put(key, value);
    }

    int putUnique(short key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, int)
     */
    @Deprecated
    default Integer putUnique(Short key, Integer value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value != null ? value.intValue() : (int) 0);
    }

    short removeValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int)
     */
    @Deprecated
    default Short removeValue(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    boolean removeValue(int value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0, key != null ? ((Short) key).shortValue() : (short) 0);
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

    @NotNull IntSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, Integer>> entrySet() {
        return ShortIntMap.super.entrySet();
    }

    @NotNull IntShortBidiMap inverse();
}
