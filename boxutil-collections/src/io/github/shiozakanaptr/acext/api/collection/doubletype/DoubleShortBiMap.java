package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortDoubleBiMap;
import io.github.shiozakanaptr.acext.api.collection.shorttype.ShortSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DoubleShortBiMap extends BiMap<Double, Short>, DoubleShortMap {
    double getKey(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(short)
     */
    @Deprecated
    default Double getKey(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    double getKeyOrDefault(short value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(short, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        if (value != null && !(value instanceof Short)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Short) value).shortValue() : (short) 0, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, short)
     */
    @Deprecated
    default Short put(Double key, Short value) {
        return DoubleShortMap.super.put(key, value);
    }

    short putUnique(double key, short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, short)
     */
    @Deprecated
    default Short putUnique(Double key, Short value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value != null ? value.shortValue() : (short) 0);
    }

    double removeValue(short value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short)
     */
    @Deprecated
    default Double removeValue(Object value) {
        if (value != null && !(value instanceof Short)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0);
    }

    boolean removeValue(short value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(short, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Short)) return false;
        return this.removeValue(value != null ? ((Short) value).shortValue() : (short) 0, key != null ? ((Double) key).doubleValue() : (double) 0);
    }

    double getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Double getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    double setDefaultReturnKey(double newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(double)
     */
    @Deprecated
    default Double setDefaultReturnKey(@Nullable Double newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.doubleValue() : (double) 0);
    }

    @NotNull ShortSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, Short>> entrySet() {
        return DoubleShortMap.super.entrySet();
    }

    @NotNull ShortDoubleBiMap inverse();
}
