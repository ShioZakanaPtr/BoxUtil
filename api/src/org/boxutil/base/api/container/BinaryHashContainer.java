package org.boxutil.base.api.container;

/**
 * A generic interface for hash-based containers that use closed/open addressing
 * or similar hashing strategies. It exposes load factor, and delegates key
 * hashing and equality decisions to a {@link HashBehavior}.
 *
 * @param <X> the type of first keys maintained by this container
 * @param <Y> the type of second keys maintained by this container
 */
public interface BinaryHashContainer<X, Y> extends Container {
    /**
     * Returns the load factor of this hash container. The load factor determines
     * how full the container can become before it is automatically resized.
     *
     * @return the load factor
     */
    float getLoadFactor();

    /**
     * Returns the {@link HashBehavior} object that controls the behavior of
     * this hash container. The behavior object is responsible for computing the
     * hash value of the first key (before masking by the table length) and for
     * determining whether two first keys are equal.
     *
     * @return the container behavior
     */
    HashBehavior<X> getFirstBehavior();

    /**
     * Returns the {@link HashBehavior} object that controls the behavior of
     * this hash container. The behavior object is responsible for computing the
     * hash value of the second key (before masking by the table length) and for
     * determining whether two second keys are equal.
     *
     * @return the container behavior
     */
    HashBehavior<Y> getSecondBehavior();
}
