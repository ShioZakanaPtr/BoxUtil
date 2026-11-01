package org.boxutil.base.api.container;

/**
 * A pair of two values, referred to as the left element and the right
 * element.
 *
 * <p>This interface is a base interface for pairs of two values. It also
 * serves as a {@link MapExt.Entry} view of the pair, where the left element is
 * treated as the key and the right element is treated as the value.
 *
 * <p>This interface is intended to be extended by specialized interfaces and
 * implemented by specialized classes, including implementations specialized
 * for primitive component types.
 *
 * <p>The left and right elements may be mutable through
 * {@code left(value)}, {@code right(value)}, or {@link #setValue(Object)}.
 * Whether mutation is supported is implementation-dependent. The default
 * implementations of the mutating methods throw
 * {@link UnsupportedOperationException}.
 *
 * @param <L> the type of the left element, which serves as the key of the
 *        {@link MapExt.Entry} view
 * @param <R> the type of the right element, which serves as the value of the
 *        {@link MapExt.Entry} view
 *
 * @apiNote As a {@link MapExt.Entry}, implementations must implement
 *          {@code equals} and {@code hashCode} according to the contracts
 *          specified by {@link MapExt.Entry}, using the left element as the key
 *          and the right element as the value.
 *
 * @see MapExt.Entry
 */
public interface Pair<L, R> extends MapExt.Entry<L, R> {
    /**
     * Returns the left element of this pair.
     *
     * @return the left element of this pair
     * @see #right()
     */
    L left();

    /**
     * Returns the left element of this pair, which serves as the key of the
     * {@link MapExt.Entry} view.
     *
     * @implSpec The default implementation returns {@link #left()}.
     *
     * @return the left element of this pair
     * @see #left()
     */
    default L getKey() {
        return this.left();
    }

    /**
     * Returns the right element of this pair.
     *
     * @return the right element of this pair
     * @see #left()
     */
    R right();

    /**
     * Returns the right element of this pair, which serves as the value of the
     * {@link MapExt.Entry} view.
     *
     * @implSpec The default implementation returns {@link #right()}.
     *
     * @return the right element of this pair
     * @see #right()
     */
    default R getValue() {
        return this.right();
    }

    /**
     * Replaces the left element of this pair with the specified value
     * (optional operation).
     *
     * @implSpec The default implementation throws
     *         {@link UnsupportedOperationException}.
     *
     * @param <T> the type of the left value
     * @param value the value to be stored in the left element
     * @return the left element previously stored in this pair
     * @throws UnsupportedOperationException if the set operation is not
     *         supported by this pair
     * @throws ClassCastException if the class of the specified value prevents
     *         it from being stored in this pair
     * @throws NullPointerException if the specified value is null and this
     *         pair does not permit null left elements
     * @throws IllegalArgumentException if some property of the specified value
     *         prevents it from being stored in this pair
     * @see #left()
     */
    default <T extends L> L left(T value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the right element of this pair with the specified value
     * (optional operation).
     *
     * @implSpec The default implementation throws
     *         {@link UnsupportedOperationException}.
     *
     * @param <T> the type of the right value
     * @param value the value to be stored in the right element
     * @return the right element previously stored in this pair
     * @throws UnsupportedOperationException if the set operation is not
     *         supported by this pair
     * @throws ClassCastException if the class of the specified value prevents
     *         it from being stored in this pair
     * @throws NullPointerException if the specified value is null and this
     *         pair does not permit null right elements
     * @throws IllegalArgumentException if some property of the specified value
     *         prevents it from being stored in this pair
     * @see #right()
     */
    default <T extends R> R right(T value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the value corresponding to this entry with the specified value
     * (optional operation).
     *
     * <p>This method replaces the right element of this pair. It is provided
     * for compatibility with the {@link MapExt.Entry} contract.
     *
     * @implSpec The default implementation returns the result of invoking
     *         {@code right(value)}.
     *
     * @param value new value to be stored in this entry
     * @return the old value corresponding to the entry
     * @throws UnsupportedOperationException if the {@code put} operation is not
     *         supported by this entry
     * @throws ClassCastException if the class of the specified value prevents
     *         it from being stored in this entry
     * @throws NullPointerException if the specified value is null and this
     *         entry does not permit null values
     * @throws IllegalArgumentException if some property of the specified value
     *         prevents it from being stored in this entry
     * @see #right()
     * @see #getValue()
     */
    default R setValue(R value) {
        return this.right(value);
    }

    /**
     * Returns whether the elements of this pair may be modified.
     *
     * <p>If this method returns {@code true}, this pair is expected to support
     * modification of its left and/or right elements through
     * {@link #left(Object)}, {@link #right(Object)}, and
     * {@link #setValue(Object)}. If this method returns {@code false}, those
     * mutating methods are expected to throw
     * {@link UnsupportedOperationException}.
     *
     * <p>This method is intended to allow callers to test, before attempting
     * a modification, whether the pair is mutable. It does not by itself
     * guarantee that every individual modification will succeed; implementations
     * may still reject specific values for reasons such as type, nullability,
     * or value constraints.
     *
     * @return {@code true} if the elements of this pair may be modified;
     *         {@code false} otherwise
     * @see #left(Object)
     * @see #right(Object)
     * @see #setValue(Object)
     */
    boolean isMutable();
}
