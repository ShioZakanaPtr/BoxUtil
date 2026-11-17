package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjDoubleBiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DoubleObjBiMap<V> extends BiMap<Double, V>, DoubleObjMap<V> {
    double getKeyRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw(Object)
     */
    @Deprecated
    default Double getKey(Object value) {
        return this.getKeyRaw(value);
    }

    double getKeyOrDefault(Object value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(Object, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        return this.getKeyOrDefault(value, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, V)
     */
    @Deprecated
    default V put(Double key, V value) {
        return DoubleObjMap.super.put(key, value);
    }

    V putUnique(double key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, V)
     */
    @Deprecated
    default V putUnique(Double key, V value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value);
    }

    double removeValueRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValueRaw(Object)
     */
    @Deprecated
    default Double removeValue(Object value) {
        return this.removeValueRaw(value);
    }

    boolean removeValue(Object value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(Object, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        return this.removeValue(value, key != null ? ((Double) key).doubleValue() : (double) 0);
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

    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, V>> entrySet() {
        return DoubleObjMap.super.entrySet();
    }

    @NotNull ObjDoubleBiMap<V> inverse();
}
