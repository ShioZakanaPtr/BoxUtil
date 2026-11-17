package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortShortBiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ShortShortBiMap extends BiMap<Short, Short>, ShortShortMap {
    short getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default Short getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    short getKeyOrDefault(short value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, short)
     */
    @Deprecated
    default Short put(Short key, Short value) {
        return ShortShortMap.super.put(key, value);
    }

    short putUnique(short key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, short)
     */
    @Deprecated
    default Short putUnique(Short key, Short value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value != null ? value.shortValue() : (short) 0);
    }

    short removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default Short removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key != null ? ((Short) key).shortValue() : (short) 0);
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

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, Short>> entrySet() {
        return ShortShortMap.super.entrySet();
    }

    @NotNull ShortShortBiMap inverse();
}
