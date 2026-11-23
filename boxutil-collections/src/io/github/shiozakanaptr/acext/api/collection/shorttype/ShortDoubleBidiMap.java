package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleShortBidiMap;
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
public interface ShortDoubleBidiMap extends BidiMap<Short, Double>, ShortDoubleMap {
    short getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Short getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    short getKeyOrDefault(double value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, double)
     */
    @Deprecated
    default Double put(Short key, Double value) {
        return ShortDoubleMap.super.put(key, value);
    }

    double putUnique(short key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, double)
     */
    @Deprecated
    default Double putUnique(Short key, Double value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value != null ? value.doubleValue() : (double) 0);
    }

    short removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Short removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Short) key).shortValue() : (short) 0);
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

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, Double>> entrySet() {
        return ShortDoubleMap.super.entrySet();
    }

    @NotNull DoubleShortBidiMap inverse();
}
