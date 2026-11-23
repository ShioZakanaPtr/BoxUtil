package io.github.shiozakanaptr.acext.api.collection.bytetype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleByteBidiMap;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleSet;
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
public interface ByteDoubleBidiMap extends BidiMap<Byte, Double>, ByteDoubleMap {
    byte getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Byte getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    byte getKeyOrDefault(double value, byte defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, byte)
     */
    @Deprecated
    default Byte getKeyOrDefault(Object value, Byte defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.byteValue() : (byte) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(byte, double)
     */
    @Deprecated
    default Double put(Byte key, Double value) {
        return ByteDoubleMap.super.put(key, value);
    }

    double putUnique(byte key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(byte, double)
     */
    @Deprecated
    default Double putUnique(Byte key, Double value) {
        return this.putUnique(key != null ? key.byteValue() : (byte) 0, value != null ? value.doubleValue() : (double) 0);
    }

    byte removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Byte removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, byte key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, byte)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Byte)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Byte) key).byteValue() : (byte) 0);
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

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Byte, Double>> entrySet() {
        return ByteDoubleMap.super.entrySet();
    }

    @NotNull DoubleByteBidiMap inverse();
}
