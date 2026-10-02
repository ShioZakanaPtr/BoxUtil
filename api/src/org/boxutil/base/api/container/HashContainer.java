package org.boxutil.base.api.container;

/**
 * A generic interface for hash-based containers that use closed/open addressing
 * or similar hashing strategies. It exposes capacity, load factor, and resizing
 * operations, and delegates key hashing and equality decisions to a {@link ContainerBehavior}.
 *
 * @param <K> the type of keys maintained by this container
 */
public interface HashContainer<K> {
    /**
     * Returns the current capacity of this hash container, typically the length
     * of the internal table.
     *
     * @return the current capacity
     */
    int getCapacity();

    /**
     * Returns the remaining space in this hash container, i.e., how many more
     * keys can be added before the container needs to be resized. This is
     * typically calculated as
     * {@code (int)(getCapacity() * getLoadFactor()) - size()}.
     *
     * @return the number of additional keys that can be added without resizing
     */
    int getRemainingSpace();

    /**
     * Returns the load factor of this hash container. The load factor determines
     * how full the container can become before it is automatically resized.
     *
     * @return the load factor
     */
    float getLoadFactor();

    /**
     * Returns the {@link ContainerBehavior} object that controls the behavior of
     * this hash container. The behavior object is responsible for computing the
     * hash value of a key (before masking by the table length) and for
     * determining whether two keys are equal.
     *
     * @return the container behavior
     */
    ContainerBehavior<K> getBehavior();

    /**
     * Rehashes this hash container to have the specified new capacity. This
     * operation recomputes the hash values of all keys and redistributes them
     * among the buckets of the new table.
     *
     * @param newCapacity the new capacity (length of the internal table)
     * @throws IllegalArgumentException if {@code newCapacity} is negative or
     *         otherwise invalid for this container
     */
    void rehash(int newCapacity);

    /**
     * Reserves space in this hash container so that it can hold at least
     * {@code newMaxSize} elements without triggering a rehash. If
     * {@code newMaxSize} is greater than the current capacity multiplied by the
     * load factor, the capacity may be increased.
     *
     * @param newMaxSize the desired maximum number of elements to hold without
     *        rehashing
     * @throws IllegalArgumentException if {@code newMaxSize} is negative
     */
    void reserve(int newMaxSize);
}
