package org.boxutil.base.api.container;

import org.boxutil.util.CalculateUtil;

/**
 * A generic interface for hash-based containers that use closed/open addressing
 * or similar hashing strategies. It exposes load factor, and delegates key
 * hashing and equality decisions to a {@link HashBehavior}.
 *
 * @param <K> the type of keys maintained by this container
 */
public interface HashContainer<K> extends Container {

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

    static int getTableCapacity(int elementSize, float loadFactor) {
        long cap = CalculateUtil.getPOTMax((long) Math.floor(elementSize / (double) loadFactor));
        if (cap < 0 || cap > (1 << 30)) throw new IllegalArgumentException("Capacity overflow");
        return (int) Math.max(cap, 2);
    }

    static int getTableMaxRemaining(int capacity, float loadFactor, boolean reservedNull) {
        int remaining = (int) Math.floor(capacity * (double) loadFactor);
        if (remaining < 0 || remaining > (1 << 30)) throw new IllegalArgumentException("Remaining overflow");
        remaining = Math.max(remaining, 1);
        return reservedNull ? remaining + 1 : remaining;
    }
}
