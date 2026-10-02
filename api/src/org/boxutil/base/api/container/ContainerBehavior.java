package org.boxutil.base.api.container;

import java.util.Objects;

/**
 * A strategy interface that defines how hash-based containers compute hash
 * values for keys and determine equality between keys. This interface is
 * functional, with the single abstract method {@link #hashing(int)} providing
 * the core hash computation.
 *
 * <p>For any object that is {@code null}, its hash code should be {@code 0}.
 * The hash code of integer {@code 0} or floating-point (under IEEE 754)
 * {@code +0.0} should also be {@code 0}; in other words, these values are
 * treated as equal to {@code null} for hashing purposes.
 *
 * @param <K> the type of keys
 */
@FunctionalInterface
public interface ContainerBehavior<K> {
    /**
     * Computes the final hash value for the given integer. The input is
     * typically the result of {@link Object#hashCode()} for a key, or the
     * primitive value itself for integer keys. Implementations should apply a
     * hash mixing function and return the result.
     *
     * @param key the input value to hash
     * @return the computed hash value
     */
    int hashing(int key);

    /**
     * Computes the hash value for the given {@code long} key by converting it
     * to an {@code int} via {@link Long#hashCode(long)} and delegating to
     * {@link #hashing(int)}.
     *
     * @param key the {@code long} key
     * @return the computed hash value
     */
    default int hashing(long key) {
        return this.hashing(Long.hashCode(key));
    }

    /**
     * Computes the hash value for the given object key. If the key is
     * {@code null}, returns {@code 0}. Otherwise, obtains the key's
     * {@link Object#hashCode()} and delegates to {@link #hashing(int)}.
     *
     * @param key the object key, possibly {@code null}
     * @return the computed hash value
     */
    default int hashing(K key) {
        return key == null ? 0 : this.hashing(key.hashCode());
    }

    /**
     * Determines whether two object keys are equal. The default implementation
     * uses {@link Objects#equals(Object, Object)}.
     *
     * @param a the first key
     * @param b the second key
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(K a, K b) {
        return Objects.equals(a, b);
    }

    /**
     * Determines whether two {@code int} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param a the first key
     * @param b the second key
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(int a, int b) {
        return a == b;
    }

    /**
     * Determines whether two {@code long} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param a the first key
     * @param b the second key
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(long a, long b) {
        return a == b;
    }

    /**
     * A hash strategy that uses the input parameter value directly as the
     * final hash, without any mixing.
     *
     * @param key the input value
     * @return the same value as {@code key}
     */
    static int hashing_Directly(int key) {
        return key;
    }

    /**
     * A hash strategy that uses {@link Object#hashCode()} as the final hash.
     * If the key is {@code null}, returns {@code 0}.
     *
     * @param key the object key, possibly {@code null}
     * @return the hash code of the key, or {@code 0} if the key is {@code null}
     */
    static int hashing_Directly(final Object key) {
        return key == null ? 0 : key.hashCode();
    }

    /**
     * A hash strategy that applies the same supplemental hash function as
     * {@link java.util.HashMap}: {@code key ^ (key >>> 16)}.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    static int hashing_HashMap(int key) {
        return key ^ (key >>> 16);
    }

    /**
     * A hash strategy that applies the same supplemental hash function as
     * {@link java.util.HashMap} to an object key. If the key is {@code null},
     * returns {@code 0}; otherwise, applies {@link #hashing_HashMap(int)} to
     * the key's {@link Object#hashCode()}.
     *
     * @param key the object key, possibly {@code null}
     * @return the mixed hash value
     */
    static int hashing_HashMap(Object key) {
        return key == null ? 0 : hashing_HashMap(key.hashCode());
    }

    /**
     * A hash strategy that applies the MurmurHash3 finalization mix to the
     * input value.
     *
     * @param key the input value
     * @return the mixed hash value
     */
    static int hashing_MurmurHash3FMix(int key) {
        key ^= key >>> 16;
        key *= 0x85ebca6b;
        key ^= key >>> 13;
        key *= 0xc2b2ae35;
        key ^= key >>> 16;
        return key;
    }

    /**
     * A hash strategy that applies the MurmurHash3 finalization mix to an
     * object key. If the key is {@code null}, returns {@code 0}; otherwise,
     * applies {@link #hashing_MurmurHash3FMix(int)} to the key's
     * {@link Object#hashCode()}.
     *
     * @param key the object key, possibly {@code null}
     * @return the mixed hash value
     */
    static int hashing_MurmurHash3FMix(final Object key) {
        return key == null ? 0 : hashing_MurmurHash3FMix(key.hashCode());
    }
}
