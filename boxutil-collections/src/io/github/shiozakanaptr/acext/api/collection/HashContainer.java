package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;

/**
 * A generic interface for hash-based containers that use closed/open addressing
 * or similar hashing strategies. It exposes load factor, and delegates key
 * hashing and equality decisions to a {@link HashBehavior}.
 *
 * <p>Implementations of this interface typically allow callers to supply a
 * custom {@link HashBehavior} in order to change how keys are hashed and
 * compared for equality. When no custom behavior is supplied, the container
 * defaults to a single, simple hashing pass, chosen to be sufficient for
 * typical use while remaining fast enough for general-purpose workloads.
 *
 * <p>If stronger guarantees are required, for example, protection against
 * hash-flooding or other adversarial inputs: callers should instead supply a
 * customized {@link HashBehavior} such as built from the {@code MurmurHash3}
 * or {@code SipHash} routines provided by
 * {@link io.github.shiozakanaptr.acext.util.HashingUtil}.
 *
 * @param <K> the type of keys maintained by this container
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
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
    @NotNull
    HashBehavior<K> getBehavior();
}
