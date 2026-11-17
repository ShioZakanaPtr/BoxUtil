package io.github.shiozakanaptr.acext.api.collection.objecttype;

import io.github.shiozakanaptr.acext.api.collection.BiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharObjBiMap;
import io.github.shiozakanaptr.acext.api.collection.chartype.CharSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ObjCharBiMap<K> extends BiMap<K, Character>, ObjCharMap<K> {
    K getKey(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKey(char)
     */
    @Deprecated
    default K getKey(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKey();
        return this.getKey(value != null ? ((Character) value).charValue() : (char) 0);
    }

    K getKeyOrDefault(char value, K defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(char, K)
     */
    @Deprecated
    default K getKeyOrDefault(Object value, K defaultKey) {
        if (value != null && !(value instanceof Character)) return defaultKey;
        return this.getKeyOrDefault(value != null ? ((Character) value).charValue() : (char) 0, defaultKey);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(K, char)
     */
    @Deprecated
    default Character put(K key, Character value) {
        return ObjCharMap.super.put(key, value);
    }

    char putUnique(K key, char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(K, char)
     */
    @Deprecated
    default Character putUnique(K key, Character value) {
        return this.putUnique(key, value != null ? value.charValue() : (char) 0);
    }

    K removeValue(char value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char)
     */
    @Deprecated
    default K removeValue(Object value) {
        if (value != null && !(value instanceof Character)) return this.getDefaultReturnKey();
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0);
    }

    boolean removeValue(char value, Object key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(char, Object)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (value != null && !(value instanceof Character)) return false;
        return this.removeValue(value != null ? ((Character) value).charValue() : (char) 0, key);
    }

    @NotNull CharSet values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<K, Character>> entrySet() {
        return ObjCharMap.super.entrySet();
    }

    @NotNull CharObjBiMap<K> inverse();
}
