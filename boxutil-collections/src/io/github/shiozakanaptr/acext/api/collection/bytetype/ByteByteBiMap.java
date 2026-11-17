package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteByteBiMap;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ByteByteBiMap extends BiMap<Byte, Byte>, ByteByteMap {
    byte getKey(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(byte)
     */
    @Deprecated
    default Byte getKey(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    byte getKeyOrDefault(byte value, byte defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(byte, byte)
     */
    @Deprecated
    default Byte getKeyOrDefault(Object value, Byte defaultKey) {
        if (value != null && !(value instanceof Byte)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Byte) value).byteValue() : (byte) 0, defaultKey != null ? defaultKey.byteValue() : (byte) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, byte)
     */
    @Deprecated
    default Byte put(Byte key, Byte value) {
        return ByteByteMap.super.put(key, value);
    }

    byte putUnique(byte key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(byte, byte)
     */
    @Deprecated
    default Byte putUnique(Byte key, Byte value) {
        return this.putUnique(key != null ? key.byteValue() : (byte) 0, value != null ? value.byteValue() : (byte) 0);
    }

    byte removeValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte)
     */
    @Deprecated
    default Byte removeValue(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    boolean removeValue(byte value, byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte, byte)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0, key != null ? ((Byte) key).byteValue() : (byte) 0);
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

    @NotNull ByteSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Byte, Byte>> entrySet() {
        return ByteByteMap.super.entrySet();
    }

    @NotNull ByteByteBiMap inverse();
}
