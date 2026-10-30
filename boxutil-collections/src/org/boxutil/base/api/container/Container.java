package org.boxutil.base.api.container;

/**
 * A generic interface for basic containers. It exposes capacity and
 * resizing function that can be control it.
 */
public interface Container {
    int MAXIMUM_CAPACITY = (1 << 31) - 9;
    int DEFAULT_INITIAL_CAPACITY = 1 << 3;

    /**
     * Resizing this container to have the specified new capacity. If this
     * container is a hash container, this operation might recompute the hash
     * values of all keys and redistributes them among the buckets of the
     * new table.
     *
     * @param newCapacity the new capacity (length of the internal table)
     * @throws UnsupportedOperationException if capacity is fixed or operation
     *         is not supported by this container
     * @throws IllegalArgumentException if {@code newCapacity} is negative or
     *         otherwise invalid for this container
     */
    void resize(int newCapacity);

    /**
     * Resizes this container to have the specified new capacity.
     *
     * <p>This method is intended for Jumbo containers, that is, containers
     * that implement {@link JumboContainer} and whose capacity may exceed
     * {@link Integer#MAX_VALUE}. If this container is a hash container, this
     * operation might recompute the hash values of all keys and redistribute
     * them among the buckets of the new table.
     *
     * @implSpec The default implementation throws
     *         {@link UnsupportedOperationException}.
     *
     * @param newCapacity the new capacity (length of the internal table)
     * @throws UnsupportedOperationException if capacity is fixed or the
     *         operation is not supported by this container
     * @throws IllegalArgumentException if {@code newCapacity} is negative or
     *         otherwise invalid for this container
     * @see #resize(int)
     * @see JumboContainer
     */
    default void resize64(long newCapacity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Reserves space in this container so that it can hold at least
     * {@code newMaxSize} elements without triggering a resize. If
     * {@code newMaxSize} is greater than the maximum store space,
     * the capacity may be increased.
     *
     * @param newMaxSize the desired maximum number of elements to hold without
     *        resizing
     * @throws UnsupportedOperationException if capacity is fixed or operation
     *         is not supported by this container
     * @throws IllegalArgumentException if {@code newMaxSize} is negative
     */
    void reserve(int newMaxSize);

    /**
     * Reserves space in this container so that it can hold at least
     * {@code newMaxSize} elements without triggering a resize.
     *
     * <p>This method is intended for Jumbo containers, that is, containers
     * that implement {@link JumboContainer} and whose capacity may exceed
     * {@link Integer#MAX_VALUE}. If {@code newMaxSize} is greater than the
     * maximum store space, the capacity may be increased.
     *
     * @implSpec The default implementation throws
     *         {@link UnsupportedOperationException}.
     *
     * @param newMaxSize the desired maximum number of elements to hold without
     *        resizing
     * @throws UnsupportedOperationException if capacity is fixed or the
     *         operation is not supported by this container
     * @throws IllegalArgumentException if {@code newMaxSize} is negative
     * @see #reserve(int)
     * @see JumboContainer
     */
    default void reserve64(long newMaxSize) {
        throw new UnsupportedOperationException();
    }

    /**
     * Shrinks the capacity of this container to the smallest capacity that is
     * sufficient to hold its current elements without requiring a resize. This
     * operation releases unused internal storage and does not remove or change
     * any elements. The exact resulting capacity is implementation-dependent
     * and may be greater than or equal to the current number of elements.
     *
     * <p>If this container is a hash container, this operation might recompute
     * the hash values of all keys and redistribute them among the buckets of the
     * new table.
     *
     * <p>If the container is already at its minimum capacity, this method may
     * have no effect.
     *
     * @throws UnsupportedOperationException if capacity is fixed or operation
     *         is not supported by this container
     * @see #resize(int)
     * @see #reserve(int)
     */
    default void shrink() {
        this.resize(2);
    }

    /**
     * Returns the current internal capacity of this container,
     * not the max store space.
     *
     * @return the current capacity, infinite or unknows if returns {@code -1}
     */
    int capacity();

    /**
     * Returns the current internal capacity of this container as a
     * {@code long}, not the maximum store space.
     *
     * <p>This method is intended for Jumbo containers, that is, containers
     * that implement {@link JumboContainer} and whose capacity may exceed
     * {@link Integer#MAX_VALUE}.
     *
     * @implSpec The default implementation returns {@code -1L}.
     *
     * @return the current capacity, or {@code -1L} if the capacity is infinite
     *         or unknown, or if long-sized capacities are not supported
     * @see #capacity()
     * @see JumboContainer
     */
    default long capacity64() {
        return -1L;
    }

    /**
     * Returns the remaining space in this hash container.
     *
     * @return the number of additional elements that can be added without
     *         resizing, infinite or unknows if returns {@code -1}
     */
    int remaining();

    /**
     * Returns the remaining space in this container as a {@code long}.
     *
     * <p>This method is intended for Jumbo containers, that is, containers
     * that implement {@link JumboContainer} and whose remaining space may
     * exceed {@link Integer#MAX_VALUE}.
     *
     * @implSpec The default implementation returns {@code -1L}.
     *
     * @return the number of additional elements that can be added without
     *         resizing, or {@code -1L} if the remaining space is infinite or
     *         unknown, or if long-sized remaining space is not supported
     * @see #remaining()
     * @see JumboContainer
     */
    default long remaining64() {
        return -1L;
    }

    /**
     * Returns the number of elements in this container as a {@code long}.
     *
     * <p>This method is intended for Jumbo containers, that is, containers
     * that implement {@link JumboContainer} and whose size may exceed
     * {@link Integer#MAX_VALUE}.
     *
     * @implSpec The default implementation returns {@code -1L}.
     *
     * @return the number of elements in this container, or {@code -1L} if the
     *         size is unknown, or if long-sized sizes are not supported
     * @see JumboContainer
     */
    default long size64() {
        return -1;
    }

    /**
     * Removes all the elements from this container (optional operation), and
     * resize to {@code newCapacity} if not an infinite capacity container.
     * The container will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *         is not supported by this container
     * @throws IllegalArgumentException if {@code newCapacity} is negative or
     *         otherwise invalid for this container
     */
    void clear(int newCapacity);

    /**
     * Removes all the elements from this container (optional operation), and
     * resizes to {@code newCapacity} if this is not an infinite-capacity
     * container. The container will be empty after this method returns.
     *
     * <p>This method is intended for Jumbo containers, that is, containers
     * that implement {@link JumboContainer} and whose capacity may exceed
     * {@link Integer#MAX_VALUE}.
     *
     * @implSpec The default implementation throws
     *         {@link UnsupportedOperationException}.
     *
     * @param newCapacity the new capacity after clearing
     * @throws UnsupportedOperationException if the {@code clear} operation is
     *         not supported by this container
     * @throws IllegalArgumentException if {@code newCapacity} is negative or
     *         otherwise invalid for this container
     * @see #clear(int)
     * @see JumboContainer
     */
    default void clear64(long newCapacity) {
        throw new UnsupportedOperationException();
    }
}
