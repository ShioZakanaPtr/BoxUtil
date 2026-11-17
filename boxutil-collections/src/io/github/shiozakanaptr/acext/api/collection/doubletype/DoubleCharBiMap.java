package io.github.shiozakanaptr.acext.api.collection.doubletype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharDoubleBiMap;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DoubleCharBiMap extends BiMap<Double, Character>, DoubleCharMap {
    double getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default Double getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    double getKeyOrDefault(char value, double defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, double)
     */
    @Deprecated
    default Double getKeyOrDefault(Object value, Double defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey != null ? defaultKey.doubleValue() : (double) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(double, char)
     */
    @Deprecated
    default Character put(Double key, Character value) {
        return DoubleCharMap.super.put(key, value);
    }

    char putUnique(double key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(double, char)
     */
    @Deprecated
    default Character putUnique(Double key, Character value) {
        return this.putUnique(key != null ? key.doubleValue() : (double) 0, value != null ? value.charValue() : (char) 0);
    }

    double removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default Double removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, double key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, double)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Double)) return false;
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key != null ? ((Double) key).doubleValue() : (double) 0);
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

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Double, Character>> entrySet() {
        return DoubleCharMap.super.entrySet();
    }

    @NotNull CharDoubleBiMap inverse();
}
