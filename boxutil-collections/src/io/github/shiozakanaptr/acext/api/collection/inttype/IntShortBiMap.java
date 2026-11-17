package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortIntBiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IntShortBiMap extends BiMap<Integer, Short>, IntShortMap {
    int getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default Integer getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    int getKeyOrDefault(short value, int defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, int)
     */
    @Deprecated
    default Integer getKeyOrDefault(Object value, Integer defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey != null ? defaultKey.intValue() : (int) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, short)
     */
    @Deprecated
    default Short put(Integer key, Short value) {
        return IntShortMap.super.put(key, value);
    }

    short putUnique(int key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(int, short)
     */
    @Deprecated
    default Short putUnique(Integer key, Short value) {
        return this.putUnique(key != null ? key.intValue() : (int) 0, value != null ? value.shortValue() : (short) 0);
    }

    int removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default Integer removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, int)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key != null ? ((Integer) key).intValue() : (int) 0);
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

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Integer, Short>> entrySet() {
        return IntShortMap.super.entrySet();
    }

    @NotNull ShortIntBiMap inverse();
}
