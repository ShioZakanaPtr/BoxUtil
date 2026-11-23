package io.github.shiozakanaptr.acext.api.collection.shorttype;

import io.github.shiozakanaptr.acext.api.collection.BidiMap;
import io.github.shiozakanaptr.acext.api.collection.EntrySetExt;
import io.github.shiozakanaptr.acext.api.collection.MapExt;
import io.github.shiozakanaptr.acext.api.collection.objecttype.ObjShortBidiMap;
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
public interface ShortObjBidiMap<V> extends BidiMap<Short, V>, ShortObjMap<V> {
    short getKeyRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyRaw(Object)
     */
    @Deprecated
    default Short getKey(Object value) {
        return this.getKeyRaw(value);
    }

    short getKeyOrDefault(Object value, short defaultKey);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #getKeyOrDefault(Object, short)
     */
    @Deprecated
    default Short getKeyOrDefault(Object value, Short defaultKey) {
        return this.getKeyOrDefault(value, defaultKey != null ? defaultKey.shortValue() : (short) 0);
    }

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #put(short, V)
     */
    @Deprecated
    default V put(Short key, V value) {
        return ShortObjMap.super.put(key, value);
    }

    V putUnique(short key, V value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #putUnique(short, V)
     */
    @Deprecated
    default V putUnique(Short key, V value) {
        return this.putUnique(key != null ? key.shortValue() : (short) 0, value);
    }

    short removeValueRaw(Object value);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValueRaw(Object)
     */
    @Deprecated
    default Short removeValue(Object value) {
        return this.removeValueRaw(value);
    }

    boolean removeValue(Object value, short key);

    /**
     * @deprecated Please use the corresponding method that using primitive type.
     * @see #removeValue(Object, short)
     */
    @Deprecated
    default boolean removeValue(Object value, Object key) {
        if (key != null && !(key instanceof Short)) return false;
        return this.removeValue(value, key != null ? ((Short) key).shortValue() : (short) 0);
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

    @NotNull ObjSet<V> values();

    /**
     * @deprecated Please use the corresponding method that using primitive or actual type.
     * @see #entrySetRaw()
     */
    @Deprecated
    default @NotNull EntrySetExt<MapExt.Entry<Short, V>> entrySet() {
        return ShortObjMap.super.entrySet();
    }

    @NotNull ObjShortBidiMap<V> inverse();
}
