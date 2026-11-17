package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortByteBiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ByteShortBiMap extends BiMap<Byte, Short>, ByteShortMap {
    byte getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default Byte getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    byte getKeyOrDefault(short value, byte defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, byte)
     */
    @Deprecated
    default Byte getKeyOrDefault(Object value, Byte defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey != null ? defaultKey.byteValue() : (byte) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, short)
     */
    @Deprecated
    default Short put(Byte key, Short value) {
        return ByteShortMap.super.put(key, value);
    }

    short putUnique(byte key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(byte, short)
     */
    @Deprecated
    default Short putUnique(Byte key, Short value) {
        return this.putUnique(key != null ? key.byteValue() : (byte) 0, value != null ? value.shortValue() : (short) 0);
    }

    byte removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default Byte removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, byte)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key != null ? ((Byte) key).byteValue() : (byte) 0);
    }

    byte getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Byte getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    byte setDefaultReturnKey(byte newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(byte)
     */
    @Deprecated
    default Byte setDefaultReturnKey(@Nullable Byte newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.byteValue() : (byte) 0);
    }

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Byte, Short>> entrySet() {
        return ByteShortMap.super.entrySet();
    }

    @NotNull ShortByteBiMap inverse();
}
