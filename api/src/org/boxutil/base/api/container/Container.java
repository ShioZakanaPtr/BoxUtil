package org.boxutil.base.api.container;

/**
 * A generic interface for basic containers. It exposes capacity and
 * resizing function that can be control it.
 */
public interface Container {
    int MAXIMUM_CAPACITY = 1 << 30;

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
     * @return the current capacity, infinite if returns {@code -1}
     */
    int capacity();

    /**
     * Returns the remaining space in this hash container.
     *
     * @return the number of additional elements that can be added without
     *         resizing, infinite if returns {@code -1}
     */
    int remaining();

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
}
