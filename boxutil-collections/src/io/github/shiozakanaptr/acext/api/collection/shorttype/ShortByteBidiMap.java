package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteShortBidiMap;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteSet;
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
public interface ShortByteBidiMap extends BidiMap<Short, Byte>, ShortByteMap {
    short getKey(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(byte)
     */
    @Deprecated
    default Short getKey(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    short getKeyOrDefault(byte value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(byte, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        if (value != null && !(value instanceof Byte)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Byte) value).byteValue() : (byte) 0, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, byte)
     */
    @Deprecated
    default Byte put(Short key, Byte value) {
        return ShortByteMap.super.put(key, value);
    }

    byte putUnique(short key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, byte)
     */
    @Deprecated
    default Byte putUnique(Short key, Byte value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value != null ? value.byteValue() : (byte) 0);
    }

    short removeValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte)
     */
    @Deprecated
    default Short removeValue(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    boolean removeValue(byte value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0, key != null ? ((Short) key).shortValue() : (short) 0);
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

    @NotNull ByteSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, Byte>> entrySet() {
        return ShortByteMap.super.entrySet();
    }

    @NotNull ByteShortBidiMap inverse();
}
