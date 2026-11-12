package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A map that maintains a bidirectional mapping between keys and values.
 *
 * <p>In addition to the normal key-to-value mapping inherited from
 * {@link MapExt}, a {@code BiMap} provides a value-to-key mapping. Each value
 * is associated with at most one key, and each key is associated with at most
 * one value; therefore the value collection returned by {@link #values()} is a
 * set, and {@link #inverse()} exposes the value-to-key direction as another
 * {@code BiMap}.
 *
 * <p>Methods such as {@link #getKey(Object)} return
 * {@link #getDefaultReturnKey()} when no mapping exists for a queried value.
 * Implementations may allow this default key to be customized through
 * {@link #setDefaultReturnKey(Object)}.
 *
 * <p>Implementations may permit {@code null} keys and/or values. If a map
 * permits {@code null} keys, a {@code null} result from
 * {@link #getKey(Object)} does not necessarily indicate that no mapping exists;
 * {@link #containsValue(Object)} may be used to distinguish these cases.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *
 * @see MapExt
 * @see java.util.Map
 */
public interface BiMap<K, V> extends MapExt<K, V> {
    /**
     * Returns the key to which the specified value is mapped in this bimap's
     * inverse direction.
     *
     * <p>More formally, if this bimap contains a mapping from a value {@code v}
     * to a key {@code k} such that {@code Objects.equals(v, value)}, then this
     * method returns {@code k}; otherwise it returns
     * {@link #getDefaultReturnKey()}.
     *
     * <p>If this bimap permits {@code null} keys, a return value of {@code null}
     * does not necessarily indicate that this bimap contains no mapping for the
     * specified value. The {@link #containsValue(Object)} operation may be used
     * to distinguish these two cases.
     *
     * @param value the value whose associated key is to be returned
     * @return the key to which the specified value is mapped, or
     *         {@link #getDefaultReturnKey()} if this bimap contains no mapping
     *         for the value
     * @throws ClassCastException if the supplied value is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied value is {@code null} and
     *         this map does not permit {@code null} values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #getKeyOrDefault(Object, Object)
     * @see #containsValue(Object)
     * @see #getDefaultReturnKey()
     */
    K getKey(Object value);

    /**
     * Returns the key to which the specified value is mapped, or the supplied
     * default key if this bimap contains no mapping for the value.
     *
     * <p>This method differs from {@link #getKey(Object)} in that it allows the
     * caller to provide a fallback key. If this bimap permits {@code null} keys,
     * this method still distinguishes a mapping to a {@code null} key from the
     * absence of a mapping by consulting {@link #containsValue(Object)}.
     *
     * @param value the value whose associated key is to be returned
     * @param defaultKey the key to return if this bimap contains no mapping for
     *        the specified value
     * @return the key to which the specified value is mapped, or
     *         {@code defaultKey} if this bimap contains no mapping for the value
     * @throws ClassCastException if the supplied value is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied value is {@code null} and
     *         this map does not permit {@code null} values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #getKey(Object)
     * @see #containsValue(Object)
     */
    default K getKeyOrDefault(Object value, K defaultKey) {
        K v;
        return ((v = this.getKey(value)) != null || this.containsValue(value)) ? v : defaultKey;
    }

    /**
     * Associates the specified value with the specified key in this bimap.
     *
     * <p>Because a {@code BiMap} enforces unique values, if the specified value
     * is already associated with a different key, the existing value-to-key
     * mapping is silently removed and the corresponding key-to-value mapping is
     * also removed, after which the specified value is associated with the
     * specified key. Likewise, if the specified key is already associated with
     * a different value, that previous mapping is replaced.
     *
     * <p>Returns the value previously associated with the specified key, or
     * {@code null} if the key had no previous mapping. A {@code null} return
     * may also indicate that the key was previously mapped to {@code null} if
     * this bimap permits {@code null} values; {@link #containsKey(Object)} may
     * be used to distinguish these cases.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the value previously associated with the specified key, or
     *         {@code null} if there was no previous mapping
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this bimap
     * @throws ClassCastException if the supplied key or value is of an
     *         inappropriate type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied key or value is {@code null}
     *         and this map does not permit {@code null} keys or values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #putUnique(Object, Object)
     */
    V put(K key, V value);

    /**
     * Associates the specified value with the specified key in this bimap,
     * provided the specified value is not already associated with a different
     * key.
     *
     * <p>Unlike {@link #put(Object, Object)}, this method does not silently
     * remove an existing value-to-key mapping. If the specified value is already
     * associated with a different key, this bimap is left unchanged and the
     * existing mapping is returned (that is, the already-associated value is
     * returned).
     *
     * <p>If the specified value is not currently associated with a different
     * key, the mapping is established and the value previously associated with
     * the specified key is returned, or {@code null} if the key had no previous
     * mapping. A {@code null} return may also indicate that the key was
     * previously mapped to {@code null} if this bimap permits {@code null}
     * values; {@link #containsKey(Object)} may be used to distinguish these
     * cases.
     *
     * @param key the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the existing value if the specified value was already associated
     *         with a different key, otherwise the value previously associated
     *         with the specified key, or {@code null} if there was no previous
     *         mapping
     * @throws UnsupportedOperationException if the {@code putUnique} operation
     *         is not supported by this bimap
     * @throws ClassCastException if the supplied key or value is of an
     *         inappropriate type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied key or value is {@code null}
     *         and this map does not permit {@code null} keys or values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #put(Object, Object)
     */
    V putUnique(K key, V value);

    /**
     * Removes the mapping for the specified value from this bimap. This operation
     * removes both the value-to-key mapping and the corresponding key-to-value
     * mapping.
     *
     * <p>Returns the key previously associated with the specified value, or
     * {@link #getDefaultReturnKey()} if this bimap contained no mapping for the
     * value.
     *
     * <p>If this bimap permits {@code null} keys, a return value of {@code null}
     * does not necessarily indicate that no mapping was removed. The
     * {@link #containsValue(Object)} operation may be used to distinguish these
     * cases.
     *
     * @param value the value whose mapping is to be removed
     * @return the key previously associated with the specified value, or
     *         {@link #getDefaultReturnKey()} if there was no mapping
     * @throws UnsupportedOperationException if the {@code removeValue} operation
     *         is not supported by this bimap
     * @throws ClassCastException if the supplied value is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied value is {@code null} and
     *         this map does not permit {@code null} values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #containsValue(Object)
     * @see #getDefaultReturnKey()
     */
    K removeValue(Object value);

    /**
     * Removes the mapping for the specified value only if it is currently
     * associated with the specified key. This operation removes both the
     * value-to-key mapping and the corresponding key-to-value mapping.
     *
     * <p>More formally, removes the mapping for {@code value} if this bimap
     * contains a value-to-key mapping for {@code value} to {@code key}; otherwise
     * returns {@code false}.
     *
     * @param value the value whose mapping is to be removed
     * @param key the key expected to be associated with the specified value
     * @return {@code true} if a mapping was removed; {@code false} otherwise
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this bimap
     * @throws ClassCastException if the supplied value or key is of an
     *         inappropriate type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied value or key is {@code null}
     *         and this map does not permit {@code null} values or keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #removeValue(Object)
     */
    default boolean removeValue(Object value, Object key) {
        Object currKey;
        if (!Objects.equals(currKey = this.getKey(key), key) || (currKey == null && !this.containsValue(value))) {
            return false;
        }
        this.removeValue(value);
        return true;
    }

    /**
     * Returns the default key used by query methods when no mapping is found for
     * a queried value.
     *
     * <p>By default, this value is {@code null}, but implementations may allow
     * customization through {@link #setDefaultReturnKey(Object)}.
     *
     * @return the default return key
     * @see #setDefaultReturnKey(Object)
     */
    default K getDefaultReturnKey() {
        return null;
    }

    /**
     * Sets the default key used by query methods when no mapping is found for a
     * queried value.
     *
     * <p>This value will be returned by query methods such as
     * {@link #getKey(Object)} when the specified value is absent, unless a
     * different fallback is supplied explicitly.
     *
     * @param newDefault the new default return key
     * @return the previous default return key
     * @throws UnsupportedOperationException if this bimap uses a fixed default
     *         return key
     * @see #getDefaultReturnKey()
     */
    default K setDefaultReturnKey(@Nullable K newDefault) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a set view of the values contained in this bimap.
     *
     * <p>Because a {@code BiMap} enforces unique values, the returned view is a
     * {@link SetExt}. The returned set is backed by this bimap, so changes to
     * this bimap are reflected in the set and vice versa, if the set supports
     * modification.
     *
     * @return a set view of the values contained in this bimap
     * @see #inverse()
     */
    @NotNull
    SetExt<V> values();

    /**
     * @see MapExt#entrySet()
     */
    @NotNull EntrySetExt<MapExt.Entry<K, V>> entrySet();

    /**
     * Returns the inverse view of this bimap, which maps this bimap's values back
     * to their associated keys.
     *
     * <p>The returned bimap is backed by this bimap, so changes to either view
     * are reflected in the other. The inverse of the inverse view is this bimap.
     *
     * @return the inverse view of this bimap
     * @see #values()
     */
    @NotNull
    BiMap<V, K> inverse();
}
