package org.boxutil.base.api.container;

/**
 * A generic interface for hash-based containers that use closed/open addressing
 * or similar hashing strategies. It exposes load factor, and delegates key
 * hashing and equality decisions to a {@link HashBehavior}.
 *
 * @param <K> the type of keys maintained by this container
 */
public interface HashContainer<K> extends Container {
    int MAXIMUM_CAPACITY = 1 << 30;
    int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    float DEFAULT_LOAD_FACTOR = 0.75f;

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
     * hash value of a key (before masking by the table length) and for
     * determining whether two keys are equal.
     *
     * @return the container behavior
     */
    HashBehavior<K> getBehavior();
}
