package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.doubletype.DoubleCharBidiMap;
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
public interface CharDoubleBidiMap extends BidiMap<Character, Double>, CharDoubleMap {
    char getKey(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(double)
     */
    @Deprecated
    default Character getKey(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.getKey(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    char getKeyOrDefault(double value, char defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(double, char)
     */
    @Deprecated
    default Character getKeyOrDefault(Object value, Character defaultKey) {
        if (value != null && !(value instanceof Double)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Double) value).doubleValue() : (double) 0, defaultKey != null ? defaultKey.charValue() : (char) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, double)
     */
    @Deprecated
    default Double put(Character key, Double value) {
        return CharDoubleMap.super.put(key, value);
    }

    double putUnique(char key, double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(char, double)
     */
    @Deprecated
    default Double putUnique(Character key, Double value) {
        return this.putUnique(key != null ? key.charValue() : (char) 0, value != null ? value.doubleValue() : (double) 0);
    }

    char removeValue(double value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double)
     */
    @Deprecated
    default Character removeValue(Object value) {
        if (value != null && !(value instanceof Double)) return this.getDefaultReturnKeyRaw();
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0);
    }

    boolean removeValue(double value, char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(double, char)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Character)) return false;
        if (value != null && !(value instanceof Double)) return false;
        return this.removeValue(value != null ? ((Double) value).doubleValue() : (double) 0, key != null ? ((Character) key).charValue() : (char) 0);
    }

    char getDefaultReturnKeyRaw();

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getDefaultReturnKeyRaw()
     */
    @Deprecated
    default Character getDefaultReturnKey() {
        return this.getDefaultReturnKeyRaw();
    }

    char setDefaultReturnKey(char newDefault);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #setDefaultReturnKey(char)
     */
    @Deprecated
    default Character setDefaultReturnKey(@Nullable Character newDefault) {
        return this.setDefaultReturnKey(newDefault != null ? newDefault.charValue() : (char) 0);
    }

    @NotNull DoubleSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Character, Double>> entrySet() {
        return CharDoubleMap.super.entrySet();
    }

    @NotNull DoubleCharBidiMap inverse();
}
