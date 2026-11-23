package io.github.shiozakanaptr.acext.api.collection.longtype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteLongBidiMap;
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
public interface LongByteBidiMap extends BidiMap<Long, Byte>, LongByteMap {
    long getKey(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(byte)
     */
    @Deprecated
    default Long getKey(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    long getKeyOrDefault(byte value, long defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(byte, long)
     */
    @Deprecated
    default Long getKeyOrDefault(Object value, Long defaultKey) {
        if (value != null && !(value instanceof Byte)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Byte) value).byteValue() : (byte) 0, defaultKey != null ? defaultKey.longValue() : (long) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(long, byte)
     */
    @Deprecated
    default Byte put(Long key, Byte value) {
        return LongByteMap.super.put(key, value);
    }

    byte putUnique(long key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(long, byte)
     */
    @Deprecated
    default Byte putUnique(Long key, Byte value) {
        return this.putUnique(key != null ? key.longValue() : (long) 0, value != null ? value.byteValue() : (byte) 0);
    }

    long removeValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte)
     */
    @Deprecated
    default Long removeValue(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    boolean removeValue(byte value, long key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte, long)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Long)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0, key != null ? ((Long) key).longValue() : (long) 0);
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

    @NotNull ByteSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Long, Byte>> entrySet() {
        return LongByteMap.super.entrySet();
    }

    @NotNull ByteLongBidiMap inverse();
}
