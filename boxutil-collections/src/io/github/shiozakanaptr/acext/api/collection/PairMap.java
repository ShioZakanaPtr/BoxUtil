package io.github.shiozakanaptr.acext.api.collection;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeConsumer;
import io.github.shiozakanaptr.acext.util.function.objecttype.TeFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A map that is keyed by an ordered pair of two keys, {@code (keyA, keyB)},
 * rather than by a single key. A {@code PairMap} is therefore a three-dimensional
 * association from {@code (X, Y)} to {@code V}.
 *
 * <p>The pair {@code (keyA, keyB)} identifies a single value, exactly as a
 * single key identifies a single value in {@link Map}. Two pairs are considered
 * to identify the same mapping when their first keys are equal and their second
 * keys are equal; that is, when {@code Objects.equals(keyA1, keyA2) &&
 * Objects.equals(keyB1, keyB2)}.
 *
 * <p>This interface intentionally does not extend {@link Map}, because
 * {@code Map} is defined over a single key type. It instead shares
 * {@link Container} with map implementations so that capacity and resizing can
 * be controlled uniformly. The views it exposes — {@link #firstKeyCollection()},
 * {@link #secondKeyCollection()}, {@link #valueCollection()} and
 * {@link #entrySet()} — mirror {@link Map#keySet()} and {@link Map#values()},
 * and the mutating operations mirror the {@code default} methods of
 * {@link Map} where a three-argument form of the operation is meaningful.
 *
 * <p><b>Null handling.</b> Typically, for any key or value that is {@code null},
 * it should be treated as {@code 0} if the key is of a specific primitive type
 * that the implementation defines. However, for any value that is {@code 0}, it
 * usually is not treated as {@code null} if the value is of a specific
 * primitive type that the implementation defines.
 *
 * <p><b>Optional operations.</b> Some implementations may not support mutation.
 * An implementation that does not support a mutating operation is documented as
 * throwing {@link UnsupportedOperationException} for that operation. Callers
 * should consult the documentation of the concrete implementation for the
 * optional operations it supports.
 *
 * <p><b>Not thread-safe.</b> Unless a concrete implementation documents
 * otherwise, a {@code PairMap} is not safe for concurrent use by multiple threads.
 * External synchronization is required if it is shared between threads.
 *
 * @param <L> the type of the first key
 * @param <R> the type of the second key
 * @param <V> the type of the mapped value
 * @see Map
 * @see MapExt
 * @see Container
 * @see PairEntry
 */
public interface PairMap<L, R, V> extends Container {
    /**
     * Returns the value to which the specified key pair is mapped, or
     * {@code null} if this map contains no mapping for the pair.
     *
     * <p>More formally, if this map contains a mapping from a pair
     * {@code (keyA, keyB)} to a value {@code v} such that
     * {@code Objects.equals(keyA, k) && Objects.equals(keyB, l)}, then this
     * method returns {@code v}; otherwise it returns {@code null}. There can be
     * at most one such mapping.
     *
     * <p>If this map permits {@code null} values, then a return value of
     * {@code null} does not necessarily indicate that this map contains no
     * mapping for the specified pair; it is also possible that the map
     * explicitly maps the pair to {@code null}. The {@link #containsKey(Object,
     * Object)} operation may be used to distinguish these two cases.
     *
     * <p>An implementation that returns a default value for absent keys should
     * document that behaviour; see {@link #getDefaultReturnValue()}.
     *
     * @param keyA the first key whose associated value is to be returned
     * @param keyB the second key whose associated value is to be returned
     * @return the value to which the specified key pair is mapped, or
     *         {@code null} if this map contains no mapping for the pair
     * @throws ClassCastException if the supplied keys are of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied keys are {@code null} and
     *         this map does not permit {@code null} keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #getOrDefault(Object, Object, Object)
     * @see #containsKey(Object, Object)
     */
    V get(Object keyA, Object keyB);

    /**
     * Returns the value to which the specified key pair is mapped, or
     * {@code defaultValue} if this map contains no mapping for the pair.
     *
     * <p>If this map permits {@code null} values, the distinction between
     * "mapped to {@code null}" and "no mapping" is made by consulting
     * {@link #containsKey(Object, Object)}, not by comparing the returned value
     * with {@code null}.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V result = get(keyA, keyB);
     * return (result != null || containsKey(keyA, keyB)) ? result : defaultValue;
     * }</pre>
     *
     * @param keyA the first key whose associated value is to be returned
     * @param keyB the second key whose associated value is to be returned
     * @param defaultValue the value to return if this map contains no mapping
     *        for the specified key pair
     * @return the value to which the specified key pair is mapped, or
     *         {@code defaultValue} if this map contains no mapping for the pair
     * @throws ClassCastException if the supplied keys are of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied keys are {@code null} and
     *         this map does not permit {@code null} keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #get(Object, Object)
     */
    default V getOrDefault(Object keyA, Object keyB, V defaultValue) {
        V result;
        return ((result = this.get(keyA, keyB)) != null || this.containsKey(keyA, keyB)) ? result : defaultValue;
    }

    /**
     * Associates the specified value with the specified key pair in this map
     * (optional operation). If the map previously contained a mapping for the
     * pair, the old value is replaced by the specified value.
     *
     * <p>Some implementations may return a default value rather than
     * {@code null} when there was no previous mapping; see
     * {@link #getDefaultReturnValue()}. Callers that need to distinguish "no
     * previous mapping" from "previous mapping to {@code null}" should call
     * {@link #containsKey(Object, Object)} beforehand or use
     * {@link #putIfAbsent(Object, Object, Object)}.
     *
     * @param keyA the first key with which the specified value is to be
     *        associated
     * @param keyB the second key with which the specified value is to be
     *        associated
     * @param value the value to be associated with the specified key pair
     * @return the previous value associated with the specified key pair, or
     *         the default return value if there was no mapping for the pair
     *         (a {@code null} return can also indicate that the map previously
     *         associated {@code null} with the pair, if the implementation
     *         supports {@code null} values)
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is
     *         {@code null} and this map does not permit {@code null} keys or
     *         values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #putIfAbsent(Object, Object, Object)
     * @see #getDefaultReturnValue()
     */
    @Nullable V put(L keyA, R keyB, V value);

    /**
     * If the specified key pair is not already associated with a value, it is
     * associated with the given value. Otherwise, the existing value is
     * returned and no change is made.
     *
     * <p>If the map permits {@code null} values, an existing mapping to
     * {@code null} is still a mapping: this method returns {@code null} and
     * does <em>not</em> replace it with {@code value}. Implementations that
     * track presence separately from the value, and that therefore cannot
     * distinguish a {@code null} value from absence through {@link
     * #get(Object, Object)} alone, should override this method.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V result = get(keyA, keyB);
     * if (result == null) {
     *     result = put(keyA, keyB, value);
     * }
     * return result;
     * }</pre>
     *
     * @param keyA the first key with which the specified value is to be
     *        associated
     * @param keyB the second key with which the specified value is to be
     *        associated
     * @param value the value to be associated with the specified key pair
     * @return the current value associated with the specified key pair, or the
     *         previous value if there was none, or {@code null} if the pair was
     *         absent and {@code value} was {@code null}
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is
     *         {@code null} and this map does not permit {@code null} keys or
     *         values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #put(Object, Object, Object)
     */
    default V putIfAbsent(L keyA, R keyB, V value) {
        V result;
        if ((result = this.get(keyA, keyB)) == null) {
            result = this.put(keyA, keyB, value);
        }
        return result;
    }

    /**
     * Removes the mapping for the specified key pair from this map if it is
     * present (optional operation).
     *
     * <p>Returns the value to which this map previously associated the key
     * pair, or the default return value if the map contained no mapping for the
     * pair.
     *
     * <p>The map will not contain a mapping for the specified pair once the
     * call returns.
     *
     * @param keyA the first key whose mapping is to be removed from the map
     * @param keyB the second key whose mapping is to be removed from the map
     * @return the previous value associated with the specified key pair, or the
     *         default return value if there was no mapping for the pair
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this map
     * @throws ClassCastException if the supplied keys are of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied keys are {@code null} and
     *         this map does not permit {@code null} keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #remove(Object, Object, Object)
     */
    V remove(Object keyA, Object keyB);

    /**
     * Removes the mapping for the specified key pair only if it is currently
     * mapped to the specified value (optional operation).
     *
     * <p>More formally, this method removes the mapping for the pair if this
     * map currently maps the pair to a value equal to {@code value}, or if the
     * map contains no mapping for the pair and {@code value} is {@code null}.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * Object currValue = get(keyA, keyB);
     * if (!Objects.equals(currValue, value) || (currValue == null && !containsKey(keyA, keyB))) {
     *     return false;
     * }
     * remove(keyA, keyB);
     * return true;
     * }</pre>
     *
     * @param keyA the first key whose mapping is to be removed from the map
     * @param keyB the second key whose mapping is to be removed from the map
     * @param value the value expected to be associated with the specified pair
     * @return {@code true} if the value was removed, {@code false} otherwise
     * @throws UnsupportedOperationException if the {@code remove} operation is
     *         not supported by this map
     * @throws ClassCastException if the supplied keys or value are of an
     *         inappropriate type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied keys or value are
     *         {@code null} and this map does not permit {@code null} keys or
     *         values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #remove(Object, Object)
     */
    default boolean remove(Object keyA, Object keyB, Object value) {
        Object currValue;
        if (!Objects.equals(currValue = this.get(keyA, keyB), value) || (currValue == null && !this.containsKey(keyA, keyB))) {
            return false;
        }
        this.remove(keyA, keyB);
        return true;
    }

    /**
     * Replaces the entry for the specified key pair only if it is currently
     * mapped to some value (optional operation).
     *
     * <p>If this map contains a mapping for the pair, the old value is replaced
     * by the specified value and the old value is returned. If there is no
     * mapping for the pair, no mapping is added and the default return value is
     * returned.
     *
     * <p>This differs from {@link Map#replace(Object, Object)}, which returns
     * {@code null} when the key is absent. An implementation may return a
     * non-{@code null} default value for absent pairs; see
     * {@link #getDefaultReturnValue()}.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V currValue = get(keyA, keyB);
     * if (currValue != null || containsKey(keyA, keyB)) {
     *     currValue = put(keyA, keyB, value);
     * }
     * return currValue;
     * }</pre>
     *
     * @param keyA the first key with which the specified value is associated
     * @param keyB the second key with which the specified value is associated
     * @param value the value to be associated with the specified key pair
     * @return the previous value associated with the specified key pair, or the
     *         default return value if there was no mapping for the pair
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is
     *         {@code null} and this map does not permit {@code null} keys or
     *         values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #replace(Object, Object, Object, Object)
     */
    default V replace(L keyA, R keyB, V value) {
        V currValue;
        if ((currValue = this.get(keyA, keyB)) != null || this.containsKey(keyA, keyB)) {
            currValue = this.put(keyA, keyB, value);
        }
        return currValue;
    }

    /**
     * Replaces the entry for the specified key pair only if it is currently
     * mapped to the specified value (optional operation).
     *
     * <p>If this map contains a mapping for the pair to a value equal to
     * {@code oldValue}, the mapping is replaced by {@code newValue} and
     * {@code true} is returned. Otherwise, no mapping is changed and
     * {@code false} is returned.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * Object currValue = get(keyA, keyB);
     * if (!Objects.equals(currValue, oldValue) || (currValue == null && !containsKey(keyA, keyB))) {
     *     return false;
     * }
     * put(keyA, keyB, newValue);
     * return true;
     * }</pre>
     *
     * @param keyA the first key with which the specified value is associated
     * @param keyB the second key with which the specified value is associated
     * @param oldValue the value expected to be associated with the specified
     *        key pair
     * @param newValue the value to be associated with the specified key pair
     * @return {@code true} if the value was replaced, {@code false} otherwise
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key or value is
     *         {@code null} and this map does not permit {@code null} keys or
     *         values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #replace(Object, Object, Object)
     */
    default boolean replace(L keyA, R keyB, V oldValue, V newValue) {
        Object currValue;
        if (!Objects.equals(currValue = this.get(keyA, keyB), oldValue) || (currValue == null && !this.containsKey(keyA, keyB))) {
            return false;
        }
        this.put(keyA, keyB, newValue);
        return true;
    }

    /**
     * If the specified key pair is not already associated with a value,
     * attempts to compute its value using the given mapping function and enters
     * it into this map unless {@code null}.
     *
     * <p>If the mapping function returns {@code null}, no mapping is recorded
     * and {@code null} is returned. If the mapping function itself throws an
     * (unchecked) exception, the exception is rethrown, and no mapping is
     * recorded.
     *
     * <p>The most common usage is to construct a new object serving as an
     * initial mapped value or memoized result.
     *
     * <p>The mapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V result = get(keyA, keyB);
     * if (result == null) {
     *     V newValue = mappingFunction.apply(keyA, keyB);
     *     if (newValue != null) {
     *         put(keyA, keyB, newValue);
     *         return newValue;
     *     }
     * }
     * return result;
     * }</pre>
     *
     * @param keyA the first key with which the computed value is to be
     *        associated
     * @param keyB the second key with which the computed value is to be
     *        associated
     * @param mappingFunction the function to compute a value
     * @return the current (existing or computed) value associated with the
     *         specified key pair, or {@code null} if the computed value is
     *         {@code null}
     * @throws NullPointerException if the specified key pair is {@code null} and
     *         this map does not permit {@code null} keys, or if
     *         {@code mappingFunction} is {@code null}
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #compute(Object, Object, TeFunction)
     */
    default V computeIfAbsent(L keyA, R keyB, @NotNull BiFunction<? super L, ? super R, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);

        V result;
        if ((result = this.get(keyA, keyB)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(keyA, keyB)) != null) {
                this.put(keyA, keyB, newValue);
                return newValue;
            }
        }
        return result;
    }

    /**
     * If the value for the specified key pair is present, attempts to compute a
     * new value given the current key pair and its value. If the remapping
     * function returns a non-{@code null} value, it is stored in this map and
     * returned; if it returns {@code null}, the mapping is removed and
     * {@code null} is returned.
     *
     * <p>If the remapping function itself throws an (unchecked) exception, the
     * exception is rethrown, and the mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V oldValue = get(keyA, keyB);
     * if (oldValue != null) {
     *     V newValue = remappingFunction.apply(keyA, keyB, oldValue);
     *     if (newValue != null) {
     *         put(keyA, keyB, newValue);
     *         return newValue;
     *     }
     *     remove(keyA, keyB);
     * }
     * return null;
     * }</pre>
     *
     * @param keyA the first key with which the value is associated
     * @param keyB the second key with which the value is associated
     * @param remappingFunction the function to compute a new value
     * @return the new value associated with the specified key pair, or
     *         {@code null} if none
     * @throws NullPointerException if {@code remappingFunction} is {@code null}
     * @throws UnsupportedOperationException if the {@code put} or {@code remove}
     *         operation is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #compute(Object, Object, TeFunction)
     */
    default V computeIfPresent(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        V oldValue;
        if ((oldValue = this.get(keyA, keyB)) != null) {
            V newValue = remappingFunction.apply(keyA, keyB, oldValue);
            if (newValue != null) {
                this.put(keyA, keyB, newValue);
                return newValue;
            } else {
                this.remove(keyA, keyB);
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Attempts to compute a mapping for the specified key pair and its current
     * mapped value, or {@code null} if there is no current mapping.
     *
     * <p>If the remapping function returns {@code null}, the mapping is removed
     * (or remains absent if initially absent). If the remapping function itself
     * throws an (unchecked) exception, the exception is rethrown, and the
     * current mapping is left unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V oldValue = get(keyA, keyB);
     * V newValue = remappingFunction.apply(keyA, keyB, oldValue);
     * if (newValue != null) {
     *     put(keyA, keyB, newValue);
     *     return newValue;
     * }
     * if (oldValue != null || containsKey(keyA, keyB)) {
     *     remove(keyA, keyB);
     * }
     * return null;
     * }</pre>
     *
     * @param keyA the first key with which the computed value is to be
     *        associated
     * @param keyB the second key with which the computed value is to be
     *        associated
     * @param remappingFunction the function to compute a value
     * @return the new value associated with the specified key pair, or
     *         {@code null} if none
     * @throws NullPointerException if {@code remappingFunction} is {@code null}
     * @throws UnsupportedOperationException if the {@code put} or {@code remove}
     *         operation is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #computeIfAbsent(Object, Object, BiFunction)
     * @see #computeIfPresent(Object, Object, TeFunction)
     */
    default V compute(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);

        V oldValue = get(keyA, keyB);
        V newValue;
        if ((newValue = remappingFunction.apply(keyA, keyB, oldValue)) == null) {
            if (oldValue != null || containsKey(keyA, keyB)) {
                remove(keyA, keyB);
            }
            return null;
        } else {
            put(keyA, keyB, newValue);
            return newValue;
        }
    }

    /**
     * If the specified key pair is not already associated with a value or is
     * associated with {@code null}, associates it with the given non-{@code
     * null} value. Otherwise, replaces the associated value with the results of
     * the given remapping function, or removes the mapping if the result is
     * {@code null}.
     *
     * <p>If the remapping function returns {@code null}, the mapping is
     * removed. If the remapping function itself throws an (unchecked)
     * exception, the exception is rethrown, and the current mapping is left
     * unchanged.
     *
     * <p>The remapping function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V oldValue = get(keyA, keyB);
     * V newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);
     * if (newValue == null) {
     *     remove(keyA, keyB);
     * } else {
     *     put(keyA, keyB, newValue);
     * }
     * return newValue;
     * }</pre>
     *
     * @param keyA the first key with which the resulting value is to be
     *        associated
     * @param keyB the second key with which the resulting value is to be
     *        associated
     * @param value the non-{@code null} value to be merged with the existing
     *        value associated with the key pair, or, if no existing value or a
     *        {@code null} value is associated with the pair, to be associated
     *        with it
     * @param remappingFunction the function to recompute a value if present
     * @return the new value associated with the specified key pair, or
     *         {@code null} if no value is associated with the pair
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if {@code value} or {@code remappingFunction}
     *         is {@code null}
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #compute(Object, Object, TeFunction)
     */
    default V merge(L keyA, R keyB, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(value);

        V oldValue = get(keyA, keyB);
        V newValue = (oldValue == null) ? value :
                remappingFunction.apply(oldValue, value);
        if (newValue == null) {
            remove(keyA, keyB);
        } else {
            put(keyA, keyB, newValue);
        }
        return newValue;
    }

    /**
     * Updates the mapping for the specified key pair using the given update
     * function. The function is applied to the current value, or to
     * {@code null} if there is no current mapping, and the resulting value is
     * associated with the pair regardless of whether it is {@code null}.
     *
     * <p>If the update function returns {@code null}, the mapping for the pair
     * is set to {@code null} (if this map permits null values); the mapping is
     * not removed. This differs from {@link #compute(Object, Object,
     * TeFunction)}, which removes the mapping when the remapping function
     * returns {@code null}.
     *
     * <p>If the specified pair is not present and this map can also return a
     * default return value, the update function receives that value instead of
     * {@code null}.
     *
     * <p>If the update function itself throws an (unchecked) exception, the
     * exception is rethrown, and the mapping is left unchanged.
     *
     * <p>The update function should not modify this map during computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V oldValue = get(keyA, keyB);
     * V newValue = updateFunction.apply(oldValue);
     * put(keyA, keyB, newValue);
     * return newValue;
     * }</pre>
     *
     * @param keyA the first key whose mapping is to be updated
     * @param keyB the second key whose mapping is to be updated
     * @param updateFunction the function to compute a new value from the
     *        current value, or from {@code null} if the pair is not present
     * @return the new value associated with the specified key pair, which may
     *         be {@code null}
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if {@code updateFunction} is {@code null}
     * @throws ClassCastException if the class of the specified key or value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of the specified key or
     *         value prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #compute(Object, Object, TeFunction)
     */
    default V update(L keyA, R keyB, @NotNull Function<? super V, ? extends V> updateFunction) {
        Objects.requireNonNull(updateFunction);

        V oldValue = this.get(keyA, keyB);
        // if the `get` method also can return a default value, directly use it
        V newValue = updateFunction.apply(oldValue);
        this.put(keyA, keyB, newValue);
        return newValue;
    }

    /**
     * Remaps the mapping for the specified key pair. If the pair is not already
     * associated with a value, returns the default return value. Otherwise,
     * computes a new key pair from the specified pair and its current value
     * using the given remapping function, and associates the current value with
     * the new pair, merging with any existing value at the new pair using the
     * given merge function when necessary.
     *
     * <p>If the remapping function returns {@code null}, the mapping for the
     * specified pair is removed and the default return value is returned. If
     * the remapping function returns the same pair, the mapping is left
     * unchanged. If the remapping function returns a different non-{@code null}
     * pair, the mapping for the specified pair is removed and the current value
     * is associated with the new pair. If the new pair already has a value, the
     * existing value and the current value are combined using the merge
     * function, even if the existing value is {@code null}.
     *
     * <p>If the merge function returns {@code null}, the mapping for the new
     * pair (if any) is removed and the default return value is returned. If
     * either function itself throws an (unchecked) exception, the exception is
     * rethrown.
     *
     * <p>The remapping and merge functions should not modify this map during
     * computation.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * V def = getDefaultReturnValue();
     * if (!containsKey(keyA, keyB)) return def;
     *
     * V currValue = get(keyA, keyB);
     * Pair<? extends X, ? extends Y> newKeyPair = remappingFunction.apply(keyA, keyB, currValue);
     * if (newKeyPair == null) {
     *     remove(keyA, keyB);
     *     return def;
     * }
     * if (!samePair) {
     *     remove(keyA, keyB);
     *     if (containsKey(newKeyPair.left(), newKeyPair.right())) {
     *         V oldValue = get(newKeyPair.left(), newKeyPair.right());
     *         V newValue = mergeFunction.apply(oldValue == def ? null : oldValue, currValue);
     *         if (newValue == null) {
     *             remove(newKeyPair.left(), newKeyPair.right());
     *             return def;
     *         }
     *         currValue = newValue;
     *     }
     *     put(newKeyPair.left(), newKeyPair.right(), currValue);
     * }
     * return currValue;
     * }</pre>
     *
     * @param keyA the first key whose mapping is to be remapped
     * @param keyB the second key whose mapping is to be remapped
     * @param remappingFunction the function to compute a new key pair from the
     *        current pair and its value
     * @param mergeFunction the function to merge the existing value at the new
     *        pair with the current value
     * @return the value associated with the resulting key pair, or the default
     *         return value if the mapping was removed
     * @throws NullPointerException if {@code remappingFunction} or
     *         {@code mergeFunction} is {@code null}
     * @throws UnsupportedOperationException if the {@code put} or {@code remove}
     *         operation is not supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see MapExt#remapping(Object, BiFunction, BiFunction)
     */
    default V remapping(L keyA, R keyB, @NotNull TeFunction<? super L, ? super R, ? super V, ? extends Pair<? extends L, ? extends R>> remappingFunction, @NotNull BiFunction<? super V, ? super V, ? extends V> mergeFunction) {
        Objects.requireNonNull(remappingFunction);
        Objects.requireNonNull(mergeFunction);

        V def = this.getDefaultReturnValue();
        if (!this.containsKey(keyA, keyB)) return def;

        V currValue = this.get(keyA, keyB);
        Pair<? extends L, ? extends R> newKeyPair = remappingFunction.apply(keyA, keyB, currValue);

        if (newKeyPair == null || !(Objects.equals(newKeyPair.left(), keyA) && Objects.equals(newKeyPair.right(), keyB))) this.remove(keyA, keyB);
        if (newKeyPair == null) return def;
        if (!(Objects.equals(newKeyPair.left(), keyA) && Objects.equals(newKeyPair.right(), keyB))) {
            if (this.containsKey(newKeyPair.left(), newKeyPair.right())) {
                V oldValue = this.get(newKeyPair.left(), newKeyPair.right()),
                        // if the `get` method also can return a default value, should check it here and use null for result, because it means "not found"
                        newValue = mergeFunction.apply(oldValue == def ? null : oldValue, currValue);
                if (newValue != null) {
                    currValue = newValue;
                } else {
                    this.remove(newKeyPair.left(), newKeyPair.right());
                    return def;
                }
            }
            this.put(newKeyPair.left(), newKeyPair.right(), currValue);
        }
        return currValue;
    }

    /**
     * Swaps the mappings between this map and the specified map. After this
     * operation, this map contains the mappings previously held by the specified
     * map, and the specified map contains the mappings previously held by this
     * map. If the specified map is the same as this map, the operation has no
     * effect.
     *
     * <p>The specified map must be compatible with this map such that all
     * mappings can be exchanged without altering their values.
     *
     * <p>Unless {@code source} is this map, this operation is a structural
     * modification. Iterators and other traversal objects previously obtained
     * from either map are considered invalid after this operation; their
     * subsequent behavior is unspecified, and they may throw
     * {@link java.util.ConcurrentModificationException}.
     *
     * @implSpec
     * The default implementation always throws
     * {@link UnsupportedOperationException}.
     *
     * @param source the map whose mappings are to be exchanged with the
     *        mappings of this map
     * @throws NullPointerException if the specified map is {@code null}
     * @throws UnsupportedOperationException if the swap operation is not
     *         supported by this map or the specified map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of a key or value in the
     *         specified map prevents it from being stored in this map, or vice
     *         versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a key or value in
     *         the specified map prevents it from being stored in this map, or
     *         vice versa
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see MapExt#swap(Map)
     */
    default void swap(@NotNull PairMap<L, R, V> source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Swaps the values associated with the specified key pairs. If both pairs
     * are present in this map, the value associated with
     * {@code (fromA, fromB)} is associated with {@code (toA, toB)}, and the
     * value associated with {@code (toA, toB)} is associated with
     * {@code (fromA, fromB)}; then {@code true} is returned. If either pair is
     * not present, this map is left unchanged and {@code false} is returned.
     *
     * <p>This operation does not change the set of keys in this map, nor does
     * it add or remove any mapping. If both pairs are present, only the values
     * associated with them are exchanged.
     *
     * <p>If the {@code put} operation throws an exception after the first value
     * has been stored, the mappings may be left in an inconsistent state.
     *
     * @implSpec
     * The default implementation is equivalent to performing the following
     * steps for this map, then returning the resulting value:
     *
     * <pre> {@code
     * if (!(containsKey(fromA, fromB) && containsKey(toA, toB))) {
     *     return false;
     * }
     * V left = get(fromA, fromB);
     * V right = get(toA, toB);
     * put(fromA, fromB, right);
     * put(toA, toB, left);
     * return true;
     * }</pre>
     *
     * @param fromA the first key of one pair to be swapped
     * @param fromB the second key of one pair to be swapped
     * @param toA the first key of the other pair to be swapped
     * @param toB the second key of the other pair to be swapped
     * @return {@code true} if both pairs were present and the values were
     *         exchanged; {@code false} otherwise
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws ClassCastException if the class of a key prevents it from being
     *         stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see MapExt#swapAt(Object, Object)
     */
    default boolean swapAt(L fromA, R fromB, L toA, R toB) {
        if (!(this.containsKey(fromA, fromB) && this.containsKey(toA, toB))) return false;
        V left = this.get(fromA, fromB);
        V right = this.get(toA, toB);
        this.put(fromA, fromB, right);
        this.put(toA, toB, left);
        return true;
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified key
     * pair.
     *
     * <p>More formally, returns {@code true} if and only if this map contains a
     * mapping for a pair {@code (keyA, keyB)} such that
     * {@code Objects.equals(keyA, k) && Objects.equals(keyB, l)}.
     *
     * @param keyA the first key whose presence in this map is to be tested
     * @param keyB the second key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified key
     *         pair
     * @throws ClassCastException if the supplied keys are of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied keys are {@code null} and
     *         this map does not permit {@code null} keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see Map#containsKey(Object)
     */
    boolean containsKey(Object keyA, Object keyB);

    /**
     * Returns {@code true} if this map contains at least one mapping whose
     * first key is equal to the specified value.
     *
     * <p>This operation tests only the first component of the compound key. It
     * is therefore a scan of the first-key index rather than a constant-time
     * lookup, unless the implementation maintains such an index.
     *
     * @param keyA the first key whose presence in this map is to be tested
     * @return {@code true} if some mapping in this map has a first key equal to
     *         {@code keyA}
     * @throws ClassCastException if the supplied key is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied key is {@code null} and this
     *         map does not permit {@code null} keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #containsKey(Object, Object)
     * @see #firstKeyCollection()
     */
    boolean containsFirstKey(Object keyA);

    /**
     * Returns {@code true} if this map contains at least one mapping whose
     * second key is equal to the specified value.
     *
     * <p>This operation tests only the second component of the compound key. It
     * is therefore a scan of the second-key index rather than a constant-time
     * lookup, unless the implementation maintains such an index.
     *
     * @param keyB the second key whose presence in this map is to be tested
     * @return {@code true} if some mapping in this map has a second key equal to
     *         {@code keyB}
     * @throws ClassCastException if the supplied key is of an inappropriate
     *         type for this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the supplied key is {@code null} and this
     *         map does not permit {@code null} keys
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see #containsKey(Object, Object)
     * @see #secondKeyCollection()
     */
    boolean containsSecondKey(Object keyB);

    /**
     * Returns {@code true} if this map maps one or more key pairs to the
     * specified value.
     *
     * <p>More formally, returns {@code true} if and only if this map contains
     * at least one mapping to a value {@code v} such that
     * {@code Objects.equals(value, v)}. This operation requires time linear in
     * the map size.
     *
     * @param value value whose presence in this map is to be tested
     * @return {@code true} if this map maps one or more key pairs to the
     *         specified value
     * @throws ClassCastException if the value is of an inappropriate type for
     *         this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is {@code null} and
     *         this map does not permit {@code null} values
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see Map#containsValue(Object)
     */
    boolean containsValue(Object value);

    /**
     * Performs the given action for each entry in this map until all entries
     * have been processed or the action throws an exception.
     *
     * <p>The action receives the first key, the second key and the value of
     * each entry, in that order. Unless otherwise specified by the
     * implementation, actions are performed in the order of iteration of
     * {@link #entrySet()}.
     *
     * <p>If the action modifies this map, the behaviour is unspecified. If the
     * action throws an exception, it is relayed to the caller.
     *
     * @param action the action to be performed for each entry
     * @throws NullPointerException if the specified action is {@code null}
     * @throws java.util.ConcurrentModificationException if an entry is found to
     *         be removed during iteration
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see Map#forEach(java.util.function.BiConsumer)
     * @see MapExt#forEach(java.util.function.BiConsumer)
     */
    void forEach(@NotNull TeConsumer<? super L, ? super R, ? super V> action);

    /**
     * Copies all of the mappings from the specified map to this map (optional
     * operation). The effect of this call is equivalent to that of calling
     * {@link #put(Object, Object, Object)} on this map once for each mapping
     * from the specified map to this map.
     *
     * <p>If the specified map is modified while the operation is in progress,
     * the behaviour of this operation is undefined.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the {@code putAll} operation is
     *         not supported by this map
     * @throws ClassCastException if the class of a key or value in the
     *         specified map prevents it from being stored in this map
     * @throws NullPointerException if the specified map is {@code null}, or if
     *         it contains a {@code null} key or value and this map does not
     *         permit {@code null} keys or values
     * @throws IllegalArgumentException if some property of a key or value in
     *         the specified map prevents it from being stored in this map
     * @see Map#putAll(Map)
     */
    void putAll(@NotNull PairMap<? extends L, ? extends R, ? extends V> m);

    /**
     * Replaces each entry's value with the result of invoking the given
     * function on that entry, until all entries have been processed or the
     * function throws an exception.
     *
     * <p>The function receives the first key, the second key and the current
     * value of each entry, and returns the replacement value. If the function
     * returns {@code null}, the entry's value is set to {@code null} if this map
     * permits null values.
     *
     * <p>If the function modifies this map, the behaviour is unspecified. If
     * the function throws an exception, it is relayed to the caller.
     *
     * @param function the function to apply to each entry
     * @throws UnsupportedOperationException if the {@code set} operation of the
     *         entry set is not supported
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified function is {@code null}
     * @throws ClassCastException if the class of a replacement value prevents
     *         it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws IllegalArgumentException if some property of a replacement value
     *         prevents it from being stored in this map
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws java.util.ConcurrentModificationException if an entry is found to
     *         be removed during iteration
     *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
     * @see Map#replaceAll(java.util.function.BiFunction)
     */
    void replaceAll(@NotNull TeFunction<? super L, ? super R, ? super V, ? extends V> function);

    /**
     * Returns the default value returned by {@link #get(Object, Object)} when
     * no mapping exists for the requested key pair, or {@code null} if this map
     * returns {@code null} for absent pairs.
     *
     * <p>Implementations that support a configurable default return value can
     * change it with {@link #setDefaultReturnValue(Object)}. The default value
     * is a convenience for callers that treat absence as a known constant; it
     * does not itself create a mapping.
     *
     * @return the default return value for absent key pairs, or {@code null}
     * @see #setDefaultReturnValue(Object)
     */
    default V getDefaultReturnValue() {
        return null;
    }

    /**
     * Sets the default value returned by {@link #get(Object, Object)} when no
     * mapping exists for the requested key pair, and returns the previous
     * default value.
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}; implementations with a fixed
     * default value do not override it.
     *
     * @param newDefault the new default return value, or {@code null} to return
     *        {@code null} for absent key pairs
     * @return the previous default return value
     * @throws UnsupportedOperationException if this map does not support a
     *         configurable default return value
     * @see #getDefaultReturnValue()
     */
    default V setDefaultReturnValue(@Nullable V newDefault) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the number of key-pair-to-value mappings in this map.
     *
     * @return the number of key-pair-to-value mappings in this map
     * @see Map#size()
     */
    int size();

    /**
     * Returns {@code true} if this map contains no key-pair-to-value mappings.
     *
     * @return {@code true} if this map contains no key-pair-to-value mappings
     * @see Map#isEmpty()
     */
    boolean isEmpty();

    /**
     * Returns a {@link CollectionExt} view of the first keys contained in this
     * map.
     *
     * <p>The collection is backed by this map, so changes to the map are
     * reflected in the collection, and vice versa. The collection supports
     * element removal, which removes the corresponding mapping from the map,
     * via {@link java.util.Iterator#remove()}, {@link
     * java.util.Collection#remove(Object)}, {@link
     * java.util.Collection#removeAll(java.util.Collection)}, {@link
     * java.util.Collection#retainAll(java.util.Collection)}, {@link
     * java.util.Collection#removeIf(java.util.function.Predicate)} and {@link
     * java.util.Collection#clear()}. It does not support the {@code add} or
     * {@code addAll} operations.
     *
     * <p>If the map is modified while an iteration over the collection is in
     * progress (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.
     *
     * @return a collection view of the first keys contained in this map
     * @see Map#keySet()
     * @see #secondKeyCollection()
     * @see #containsFirstKey(Object)
     */
    @NotNull
    CollectionExt<L> firstKeyCollection();

    /**
     * Returns a {@link CollectionExt} view of the second keys contained in this
     * map.
     *
     * <p>The collection is backed by this map, so changes to the map are
     * reflected in the collection, and vice versa. The collection supports
     * element removal, which removes the corresponding mapping from the map,
     * via {@link java.util.Iterator#remove()}, {@link
     * java.util.Collection#remove(Object)}, {@link
     * java.util.Collection#removeAll(java.util.Collection)}, {@link
     * java.util.Collection#retainAll(java.util.Collection)}, {@link
     * java.util.Collection#removeIf(java.util.function.Predicate)} and {@link
     * java.util.Collection#clear()}. It does not support the {@code add} or
     * {@code addAll} operations.
     *
     * <p>If the map is modified while an iteration over the collection is in
     * progress (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.
     *
     * @return a collection view of the second keys contained in this map
     * @see Map#keySet()
     * @see #firstKeyCollection()
     * @see #containsSecondKey(Object)
     */
    @NotNull
    CollectionExt<R> secondKeyCollection();

    /**
     * Returns a {@link CollectionExt} view of the values contained in this map.
     *
     * <p>The collection is backed by this map, so changes to the map are
     * reflected in the collection, and vice versa. The collection supports
     * element removal, which removes the corresponding mapping from the map,
     * via {@link java.util.Iterator#remove()}, {@link
     * java.util.Collection#remove(Object)}, {@link
     * java.util.Collection#removeAll(java.util.Collection)}, {@link
     * java.util.Collection#retainAll(java.util.Collection)}, {@link
     * java.util.Collection#removeIf(java.util.function.Predicate)} and {@link
     * java.util.Collection#clear()}. It does not support the {@code add} or
     * {@code addAll} operations.
     *
     * <p>If the map is modified while an iteration over the collection is in
     * progress (except through the iterator's own {@code remove} operation),
     * the results of the iteration are undefined.
     *
     * @return a collection view of the values contained in this map
     * @see Map#values()
     */
    @NotNull
    CollectionExt<V> valueCollection();

    /**
     * Returns an {@link EntrySetExt} view of the mappings contained in this
     * map.
     *
     * <p>Each element of the returned set is a {@link PairEntry} that exposes the
     * first key, the second key and the value of one mapping.
     *
     * <p>The set is backed by this map, so changes to the map are reflected in
     * the set, and vice versa. The set supports element removal, which removes
     * the corresponding mapping from this map, via {@link
     * java.util.Iterator#remove()}, {@link java.util.Set#remove(Object)},
     * {@link java.util.Set#removeAll(java.util.Collection)}, {@link
     * java.util.Set#retainAll(java.util.Collection)}, {@link
     * java.util.Set#removeIf(java.util.function.Predicate)} and {@link
     * java.util.Set#clear()}. It does not support the {@code add} or
     * {@code addAll} operations.
     *
     * <p>If the map is modified while an iteration over the set is in progress
     * (except through the iterator's own {@code remove} operation, or through
     * the {@code setValue} operation on an entry returned by the iterator), the
     * results of the iteration are undefined.
     *
     * @return a set view of the mappings contained in this map
     * @see Map#entrySet()
     * @see PairEntry
     */
    @NotNull
    EntrySetExt<PairEntry<L, R, V>> entrySet();

    /**
     * Removes all of the mappings from this map (optional operation). The map
     * will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation is
     *         not supported by this map
     * @see Map#clear()
     */
    void clear();

    /**
     * Returns the hash code value for this map.
     *
     * <p>The hash code of a map is defined to be the sum of the hash codes of
     * each entry in the map's {@link #entrySet()} view. This ensures that two
     * maps that are equal according to {@link #equals(Object)} have the same
     * hash code.
     *
     * @return the hash code value for this map
     * @see Map#hashCode()
     * @see #equals(Object)
     */
    int hashCode();

    /**
     * Compares the specified object with this map for equality.
     *
     * <p>Returns {@code true} if the given object is also a {@code PairMap}, the
     * two maps represent the same mappings, and the entry sets are equal.
     * More formally, two maps {@code m1} and {@code m2} represent the same
     * mappings if {@code m1.entrySet().equals(m2.entrySet())}.
     *
     * <p>This implementation first checks whether the given object is
     * {@code this}; if so, it returns {@code true}. It then checks whether the
     * given object is a {@code PairMap}; if not, it returns {@code false}. It then
     * checks whether the two maps have the same size; if not, it returns
     * {@code false}. It then iterates over the entries of this map, checking
     * that the specified map contains each mapping.
     *
     * @param o object to be compared for equality with this map
     * @return {@code true} if the specified object is equal to this map
     * @see Map#equals(Object)
     * @see #hashCode()
     */
    boolean equals(Object o);

    /**
     * Returns the string representation of this map.
     *
     * <p>The string representation consists of a list of key-pair-to-value
     * mappings enclosed in braces ({@code "{}"}).
     *
     * @return the string representation of this map
     * @see Object#toString()
     */
    String toString();

    /**
     * A single key-pair-to-value mapping of a {@link PairMap}.
     *
     * <p>A {@code BiEntry} exposes the two components of the compound key
     * through {@link #getFirstKey()} and {@link #getSecondKey()}, and the mapped
     * value through {@link #getValue()}. The value can be changed through
     * {@link #setValue(Object)}, which writes through to the backing map in the
     * same way as {@link Map.Entry#setValue(Object)} does.
     *
     * <p>An entry may become invalid after the map it came from is structurally
     * modified. The behaviour of the accessors on an invalidated entry is
     * unspecified unless the implementation documents otherwise.
     *
     * @param <X> the type of the first key
     * @param <Y> the type of the second key
     * @param <V> the type of the mapped value
     * @see PairMap#entrySet()
     * @see Entry
     */
    interface PairEntry<X, Y, V> {
        /**
         * Returns the first key of this entry.
         *
         * @return the first key of this entry
         * @see Entry#getKey()
         */
        X getFirstKey();

        /**
         * Returns the second key of this entry.
         *
         * @return the second key of this entry
         * @see Entry#getKey()
         */
        Y getSecondKey();

        /**
         * Returns the value of this entry.
         *
         * @return the value of this entry
         * @see Entry#getValue()
         */
        V getValue();

        /**
         * Replaces the value corresponding to this entry with the specified
         * value (optional operation). Writes through to the backing map.
         *
         * @param newValue the new value to be stored in this entry
         * @return the old value corresponding to the entry
         * @throws UnsupportedOperationException if the {@code setValue}
         *         operation is not supported by the backing map
         *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
         * @throws ClassCastException if the class of the specified value
         *         prevents it from being stored in the backing map
         *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
         * @throws NullPointerException if the specified value is {@code null}
         *         and the backing map does not permit {@code null} values
         *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
         * @throws IllegalArgumentException if some property of the specified
         *         value prevents it from being stored in the backing map
         *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
         * @throws IllegalStateException if this entry has been removed from the
         *         backing map
         *         (<a href="{@docRoot}/java.base/java/util/Collection.html#optional-restrictions">optional</a>)
         * @see Entry#setValue(Object)
         */
        V setValue(V newValue);

        /**
         * Compares the specified object with this entry for equality.
         *
         * <p>Returns {@code true} if the given object is also a {@code BiEntry}
         * and both entries have equal first keys, equal second keys and equal
         * values.
         *
         * @param o object to be compared for equality with this entry
         * @return {@code true} if the specified object is equal to this entry
         * @see Entry#equals(Object)
         * @see #hashCode()
         */
        boolean equals(Object o);

        /**
         * Returns the hash code value for this entry.
         *
         * <p>The hash code of a {@code BiEntry} is defined to be
         * {@code Objects.hashCode(getFirstKey()) ^ Objects.hashCode(getSecondKey())
         * ^ Objects.hashCode(getValue())}, mirroring the definition of
         * {@link Entry#hashCode()} extended to two keys.
         *
         * @return the hash code value for this entry
         * @see Entry#hashCode()
         * @see #equals(Object)
         */
        int hashCode();
    }
}
