package io.github.shiozakanaptr.acext.api.collection.chartype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjCharBidiMap;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjSet;
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
public interface CharObjBidiMap<V> extends BidiMap<Character, V>, CharObjMap<V> {
    char getKeyRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw(Object)
     */
    @Deprecated
    default Character getKey(Object value) {
        return this.getKeyRaw(value);
    }

    char getKeyOrDefault(Object value, char defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(Object, char)
     */
    @Deprecated
    default Character getKeyOrDefault(Object value, Character defaultKey) {
        return this.getKeyOrDefault(value, defaultKey != null ? defaultKey.charValue() : (char) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(char, V)
     */
    @Deprecated
    default V put(Character key, V value) {
        return CharObjMap.super.put(key, value);
    }

    V putUnique(char key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(char, V)
     */
    @Deprecated
    default V putUnique(Character key, V value) {
        return this.putUnique(key != null ? key.charValue() : (char) 0, value);
    }

    char removeValueRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValueRaw(Object)
     */
    @Deprecated
    default Character removeValue(Object value) {
        return this.removeValueRaw(value);
    }

    boolean removeValue(Object value, char key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(Object, char)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Character)) return false;
        return this.removeValue(value, key != null ? ((Character) key).charValue() : (char) 0);
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

    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Character, V>> entrySet() {
        return CharObjMap.super.entrySet();
    }

    @NotNull ObjCharBidiMap<V> inverse();
}
