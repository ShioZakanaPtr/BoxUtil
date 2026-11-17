package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjByteBiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ByteObjBiMap<V> extends BiMap<Byte, V>, ByteObjMap<V> {
    byte getKeyRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw(Object)
     */
    @Deprecated
    default Byte getKey(Object value) {
        return this.getKeyRaw(value);
    }

    byte getKeyOrDefault(Object value, byte defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(Object, byte)
     */
    @Deprecated
    default Byte getKeyOrDefault(Object value, Byte defaultKey) {
        return this.getKeyOrDefault(value, defaultKey != null ? defaultKey.byteValue() : (byte) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, V)
     */
    @Deprecated
    default V put(Byte key, V value) {
        return ByteObjMap.super.put(key, value);
    }

    V putUnique(byte key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(byte, V)
     */
    @Deprecated
    default V putUnique(Byte key, V value) {
        return this.putUnique(key != null ? key.byteValue() : (byte) 0, value);
    }

    byte removeValueRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValueRaw(Object)
     */
    @Deprecated
    default Byte removeValue(Object value) {
        return this.removeValueRaw(value);
    }

    boolean removeValue(Object value, byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(Object, byte)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        return this.removeValue(value, key != null ? ((Byte) key).byteValue() : (byte) 0);
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

    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Byte, V>> entrySet() {
        return ByteObjMap.super.entrySet();
    }

    @NotNull ObjByteBiMap<V> inverse();
}
