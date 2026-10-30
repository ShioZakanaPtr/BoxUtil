package org.boxutil.base.api.container;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
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
public interface HashBehavior<K> extends Serializable {
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
     * Computes the hash value for the given {@code byte} key by converting it
     * to an {@code int} via {@link Byte#hashCode(byte)} and delegating to
     * {@link #hashing(int)}.
     *
     * @param key the {@code byte} key
     * @return the computed hash value
     */
    default int hashing(byte key) {
        return this.hashing(Byte.hashCode(key));
    }

    /**
     * Computes the hash value for the given {@code short} key by converting it
     * to an {@code int} via {@link Short#hashCode(short)} and delegating to
     * {@link #hashing(int)}.
     *
     * @param key the {@code short} key
     * @return the computed hash value
     */
    default int hashing(short key) {
        return this.hashing(Short.hashCode(key));
    }

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
     * Computes the hash value for the given {@code char} key by converting it
     * to an {@code int} via {@link Character#hashCode(char)} and delegating to
     * {@link #hashing(int)}.
     *
     * @param key the {@code char} key
     * @return the computed hash value
     */
    default int hashing(char key) {
        return this.hashing(Character.hashCode(key));
    }

    /**
     * Computes the hash value for the given {@code float} key by converting it
     * to an {@code int} via {@link Float#hashCode(float)} and delegating to
     * {@link #hashing(int)}.
     *
     * @param key the {@code float} key
     * @return the computed hash value
     */
    default int hashing(float key) {
        return this.hashing(Float.floatToRawIntBits(key));
    }

    /**
     * Computes the hash value for the given {@code double} key by converting it
     * to an {@code int} via {@link Double#hashCode(double)} and delegating to
     * {@link #hashing(int)}.
     *
     * @param key the {@code double} key
     * @return the computed hash value
     */
    default int hashing(double key) {
        return this.hashing(Double.doubleToRawLongBits(key));
    }

    /**
     * Computes the hash value for the given object key. If the key is
     * {@code null}, returns {@code 0}. Otherwise, obtains the key's
     * {@link Object#hashCode()} and delegates to {@link #hashing(int)}.
     *
     * @param key the object key, possibly {@code null}
     * @return the computed hash value
     */
    default int hashing(@Nullable Object key) {
        return key == null ? 0 : this.hashing(key.hashCode());
    }

    /**
     * Determines whether two object keys are equal. The default implementation
     * uses {@link Objects#equals(Object, Object)}.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(@Nullable Object src, @Nullable K dst) {
        return Objects.equals(src, dst);
    }

    /**
     * Determines whether two {@code byte} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(byte src, byte dst) {
        return src == dst;
    }

    /**
     * Determines whether two {@code short} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(short src, short dst) {
        return src == dst;
    }

    /**
     * Determines whether two {@code int} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(int src, int dst) {
        return src == dst;
    }

    /**
     * Determines whether two {@code long} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(long src, long dst) {
        return src == dst;
    }

    /**
     * Determines whether two {@code char} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(char src, char dst) {
        return src == dst;
    }

    /**
     * Determines whether two {@code float} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(float src, float dst) {
        return src == dst;
    }

    /**
     * Determines whether two {@code double} keys are equal. The default
     * implementation uses the {@code ==} operator.
     *
     * @param src the input query key
     * @param dst the existed key in container or special value
     * @return {@code true} if the keys are equal; {@code false} otherwise
     */
    default boolean equals(double src, double dst) {
        return src == dst;
    }

    /**
     * Mix the hash value of two input keys. The default implementation
     * is {@code left + right * 31}.
     *
     * <p>For multi key such as {@link BiMap}, will only use the first behavior
     * to mix all the keys.
     *
     * @param left the first input hash
     * @param right the second input hash
     * @return the mixed hash about input keys
     */
    default int mix(int left, int right) {
        return left + right * 31;
    }
}
