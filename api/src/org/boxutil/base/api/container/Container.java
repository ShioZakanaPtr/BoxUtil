package org.boxutil.base.api.container;

/**
 * A generic interface for basic containers. It exposes capacity and
 * resizing function that can be control it.
 */
public interface Container {
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
     * Returns the current internal capacity of this container,
     * not the max store space.
     *
     * @return the current capacity, infinite if returns {@code -1}
     */
    int getCapacity();

    /**
     * Returns the remaining space in this hash container.
     *
     * @return the number of additional elements that can be added without
     *         resizing, infinite if returns {@code -1}
     */
    int getRemainingSpace();
}
