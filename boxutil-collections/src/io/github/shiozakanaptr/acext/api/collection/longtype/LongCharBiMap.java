package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharLongBiMap;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LongCharBiMap extends BiMap<Long, Character>, LongCharMap {
    long getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default Long getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    long getKeyOrDefault(char value, long defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, long)
     */
    @Deprecated
    default Long getKeyOrDefault(Object value, Long defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey != null ? defaultKey.longValue() : (long) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, char)
     */
    @Deprecated
    default Character put(Long key, Character value) {
        return LongCharMap.super.put(key, value);
    }

    char putUnique(long key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(long, char)
     */
    @Deprecated
    default Character putUnique(Long key, Character value) {
        return this.putUnique(key != null ? key.longValue() : (long) 0, value != null ? value.charValue() : (char) 0);
    }

    long removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default Long removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, long)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key != null ? ((Long) key).longValue() : (long) 0);
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

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Long, Character>> entrySet() {
        return LongCharMap.super.entrySet();
    }

    @NotNull CharLongBiMap inverse();
}
