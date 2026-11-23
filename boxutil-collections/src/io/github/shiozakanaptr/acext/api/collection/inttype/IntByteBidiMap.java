package io.github.shiozakanaptr.acext.api.collection.inttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.bytetype.ByteIntBidiMap;
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
public interface IntByteBidiMap extends BidiMap<Integer, Byte>, IntByteMap {
    int getKey(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(byte)
     */
    @Deprecated
    default Integer getKey(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    int getKeyOrDefault(byte value, int defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(byte, int)
     */
    @Deprecated
    default Integer getKeyOrDefault(Object value, Integer defaultKey) {
        if (value != null && !(value instanceof Byte)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Byte) value).byteValue() : (byte) 0, defaultKey != null ? defaultKey.intValue() : (int) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(int, byte)
     */
    @Deprecated
    default Byte put(Integer key, Byte value) {
        return IntByteMap.super.put(key, value);
    }

    byte putUnique(int key, byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(int, byte)
     */
    @Deprecated
    default Byte putUnique(Integer key, Byte value) {
        return this.putUnique(key != null ? key.intValue() : (int) 0, value != null ? value.byteValue() : (byte) 0);
    }

    int removeValue(byte value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte)
     */
    @Deprecated
    default Integer removeValue(Object value) {
        if (value != null && !(value instanceof Byte)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0);
    }

    boolean removeValue(byte value, int key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(byte, int)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Integer)) return false;
        if (value != null && !(value instanceof Byte)) return false;
        return this.removeValue(value != null ? ((Byte) value).byteValue() : (byte) 0, key != null ? ((Integer) key).intValue() : (int) 0);
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

    @NotNull ByteSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Integer, Byte>> entrySet() {
        return IntByteMap.super.entrySet();
    }

    @NotNull ByteIntBidiMap inverse();
}
