package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongDoubleBidiMap;
import io.github.shiozakanaptr.acext.api.collection.longtype.LongSet;
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
public interface DoubleLongBidiMap extends BidiMap<Double, Long>, DoubleLongMap {
    double getKey(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(long)
     */
    @Deprecated
    default Double getKey(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Long) value).longValue() : (long) 0);
    }

    double getKeyOrDefault(long value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(long, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        if (value != null && !(value instanceof Long)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Long) value).longValue() : (long) 0, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, long)
     */
    @Deprecated
    default Long put(Double key, Long value) {
        return DoubleLongMap.super.put(key, value);
    }

    long putUnique(double key, long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, long)
     */
    @Deprecated
    default Long putUnique(Double key, Long value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value != null ? value.longValue() : (long) 0);
    }

    double removeValue(long value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long)
     */
    @Deprecated
    default Double removeValue(Object value) {
        if (value != null && !(value instanceof Long)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0);
    }

    boolean removeValue(long value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(long, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Long)) return false;
        return this.removeValue(value != null ? ((Long) value).longValue() : (long) 0, key != null ? ((Double) key).doubleValue() : (double) 0);
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

    @NotNull LongSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, Long>> entrySet() {
        return DoubleLongMap.super.entrySet();
    }

    @NotNull LongDoubleBidiMap inverse();
}
