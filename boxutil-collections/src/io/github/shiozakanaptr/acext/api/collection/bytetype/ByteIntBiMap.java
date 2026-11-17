package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntByteBiMap;
import io.github.shiozakanaptr.acext.api.collection.inttype.IntSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ByteIntBiMap extends BiMap<Byte, Integer>, ByteIntMap {
    byte getKey(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(int)
     */
    @Deprecated
    default Byte getKey(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    byte getKeyOrDefault(int value, byte defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(int, byte)
     */
    @Deprecated
    default Byte getKeyOrDefault(Object value, Byte defaultKey) {
        if (value != null && !(value instanceof Integer)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Integer) value).intValue() : (int) 0, defaultKey != null ? defaultKey.byteValue() : (byte) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, int)
     */
    @Deprecated
    default Integer put(Byte key, Integer value) {
        return ByteIntMap.super.put(key, value);
    }

    int putUnique(byte key, int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(byte, int)
     */
    @Deprecated
    default Integer putUnique(Byte key, Integer value) {
        return this.putUnique(key != null ? key.byteValue() : (byte) 0, value != null ? value.intValue() : (int) 0);
    }

    byte removeValue(int value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int)
     */
    @Deprecated
    default Byte removeValue(Object value) {
        if (value != null && !(value instanceof Integer)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0);
    }

    boolean removeValue(int value, byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(int, byte)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Integer)) return false;
        return this.removeValue(value != null ? ((Integer) value).intValue() : (int) 0, key != null ? ((Byte) key).byteValue() : (byte) 0);
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

    @NotNull IntSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Byte, Integer>> entrySet() {
        return ByteIntMap.super.entrySet();
    }

    @NotNull IntByteBiMap inverse();
}
