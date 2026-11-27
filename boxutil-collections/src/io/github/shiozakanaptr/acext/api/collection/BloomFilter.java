package io.github.shiozakanaptr.acext.api.collection;

import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * A probabilistic data structure that tests whether an element is possibly
 * contained in a set while using a bounded amount of memory.
 *
 * <p>A Bloom filter may report a false positive: {@link #contains} may return
 * {@code true} for an element that was not added. Unless a concrete
 * implementation documents otherwise, it should not report a false negative
 * for an element that has been added.
 *
 * <p>This interface provides overloads of {@link #add} and {@link #contains}
 * for {@code int}, {@code long}, {@code float}, {@code double}, and arbitrary
 * objects. It also extends {@link java.util.function.Predicate}, so an instance
 * can be used directly as a predicate; the default {@link #test(Object)} method
 * delegates to {@link #contains(Object)}.
 *
 * <p><b>Fixed capacity:</b> A Bloom filter is fixed-capacity by design. An
 * implementation must be constructed with its final capacity and must not
 * change that capacity afterward. Consequently, {@link #resize(int)},
 * {@link #reserve(int)}, {@link #shrink()}, and {@link #clear(int)} are not
 * allowed to be invoked on a {@code BloomFilter}; the default implementations
 * of these methods throw {@link UnsupportedOperationException}. Implementations
 * must not override these methods with functional implementations. Use
 * {@link Container#clear()} to remove all elements when the implementation
 * supports it.
 *
 * <p><b>Thread safety:</b> Implementations of this interface may be thread-safe
 * or not thread-safe. Unless a concrete implementation documents otherwise,
 * callers must provide external synchronization when a filter is accessed
 * concurrently. Consult the documentation of the concrete implementation for
 * its exact thread-safety, atomicity, and consistency guarantees.
 *
 * <p><b>Memory:</b> The internal data of an implementation may be stored on the
 * Java heap or in off-heap (direct) memory. Implementations that use off-heap
 * memory may require explicit release; callers should consult the
 * documentation of the concrete implementation to determine whether off-heap
 * memory is used and whether {@link Container#clear()} or another method must
 * be called to release it.
 *
 * <p><b>Capacity and size:</b> The capacity of a Bloom filter is typically
 * larger than {@link Integer#MAX_VALUE}, so a long-valued capacity is exposed
 * through {@link #capacity64()}; {@link #capacity()} clamps the result to the
 * {@code int} range. {@link #remaining()} is not applicable to Bloom filters
 * and returns {@code -1} to indicate that the remaining capacity is unknown or
 * not applicable. {@link #size()} returns the number of elements added, not the
 * number of bits used by the filter.
 *
 * @param <E> the type of elements tested by this filter
 *
 * @see Container
 * @see java.util.function.Predicate
 * @see HashBehavior
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public interface BloomFilter<E> extends Predicate<E>, Container {
    /**
     * Unsupported. A {@code BloomFilter} is fixed-capacity and must not be
     * resized after construction.
     *
     * <p>This method is provided only to complete the resizing surface shared
     * with {@link Container}. It always throws
     * {@link UnsupportedOperationException} and must not be overridden with a
     * functional implementation.
     *
     * @param newCapacity ignored
     * @throws UnsupportedOperationException always
     */
    default void resize(int newCapacity) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported. A {@code BloomFilter} is fixed-capacity and must not be
     * reserved after construction.
     *
     * <p>This method is provided only to complete the resizing surface shared
     * with {@link Container}. It always throws
     * {@link UnsupportedOperationException} and must not be overridden with a
     * functional implementation.
     *
     * @param newMaxSize ignored
     * @throws UnsupportedOperationException always
     */
    default void reserve(int newMaxSize) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported. A {@code BloomFilter} is fixed-capacity and must not be
     * shrunk after construction.
     *
     * <p>This method is provided only to complete the resizing surface shared
     * with {@link Container}. It always throws
     * {@link UnsupportedOperationException} and must not be overridden with a
     * functional implementation.
     *
     * @throws UnsupportedOperationException always
     */
    default void shrink() {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds the specified {@code int} element to this filter and reports whether
     * it was definitely absent before this call.
     *
     * @param element the element to add
     * @return {@code true} if the element was definitely not contained in this
     *         filter before this call; {@code false} if it may have been
     *         contained before this call (possibly due to a false positive)
     */
    boolean add(int element);

    /**
     * Adds the specified {@code long} element to this filter and reports
     * whether it was definitely absent before this call.
     *
     * @param element the element to add
     * @return {@code true} if the element was definitely not contained in this
     *         filter before this call; {@code false} if it may have been
     *         contained before this call (possibly due to a false positive)
     */
    boolean add(long element);

    /**
     * Adds the specified {@code float} element to this filter and reports
     * whether it was definitely absent before this call.
     *
     * @param element the element to add
     * @return {@code true} if the element was definitely not contained in this
     *         filter before this call; {@code false} if it may have been
     *         contained before this call (possibly due to a false positive)
     */
    boolean add(float element);

    /**
     * Adds the specified {@code double} element to this filter and reports
     * whether it was definitely absent before this call.
     *
     * @param element the element to add
     * @return {@code true} if the element was definitely not contained in this
     *         filter before this call; {@code false} if it may have been
     *         contained before this call (possibly due to a false positive)
     */
    boolean add(double element);

    /**
     * Adds the specified element to this filter and reports whether it was
     * definitely absent before this call.
     *
     * <p>Implementations that do not support {@code null} elements may throw
     * {@link NullPointerException}.
     *
     * @param element the element to add
     * @return {@code true} if the element was definitely not contained in this
     *         filter before this call; {@code false} if it may have been
     *         contained before this call (possibly due to a false positive)
     * @throws NullPointerException if the implementation does not support
     *         {@code null} elements and {@code element} is {@code null}
     */
    boolean add(E element);

    /**
     * Returns {@code true} if this filter might contain the specified
     * {@code int} element. A return value of {@code true} indicates a possible
     * match and may be a false positive; a return value of {@code false}
     * indicates that the element was not added, unless the implementation
     * documents otherwise.
     *
     * @param element the element to test
     * @return {@code true} if this filter might contain the element;
     *         {@code false} if it definitely does not
     */
    boolean contains(int element);

    /**
     * Returns {@code true} if this filter might contain the specified
     * {@code long} element. A return value of {@code true} indicates a possible
     * match and may be a false positive; a return value of {@code false}
     * indicates that the element was not added, unless the implementation
     * documents otherwise.
     *
     * @param element the element to test
     * @return {@code true} if this filter might contain the element;
     *         {@code false} if it definitely does not
     */
    boolean contains(long element);

    /**
     * Returns {@code true} if this filter might contain the specified
     * {@code float} element. A return value of {@code true} indicates a
     * possible match and may be a false positive; a return value of
     * {@code false} indicates that the element was not added, unless the
     * implementation documents otherwise.
     *
     * @param element the element to test
     * @return {@code true} if this filter might contain the element;
     *         {@code false} if it definitely does not
     */
    boolean contains(float element);

    /**
     * Returns {@code true} if this filter might contain the specified
     * {@code double} element. A return value of {@code true} indicates a
     * possible match and may be a false positive; a return value of
     * {@code false} indicates that the element was not added, unless the
     * implementation documents otherwise.
     *
     * @param element the element to test
     * @return {@code true} if this filter might contain the element;
     *         {@code false} if it definitely does not
     */
    boolean contains(double element);

    /**
     * Returns {@code true} if this filter might contain the specified object
     * element. A return value of {@code true} indicates a possible match and
     * may be a false positive; a return value of {@code false} indicates that
     * the element was not added, unless the implementation documents otherwise.
     *
     * <p>Implementations that do not support {@code null} elements may throw
     * {@link NullPointerException}.
     *
     * @param element the element to test
     * @return {@code true} if this filter might contain the element;
     *         {@code false} if it definitely does not
     */
    boolean contains(Object element);

    /**
     * Tests whether the specified element might be contained in this filter.
     *
     * <p>The default implementation delegates to {@link #contains(Object)}.
     *
     * @param e the element to test
     * @return {@code true} if this filter might contain the element;
     *         {@code false} if it definitely does not
     */
    default boolean test(E e) {
        return this.contains(e);
    }

    /**
     * Returns the capacity of this filter as an {@code int}.
     *
     * <p>Bloom filter capacities commonly exceed {@link Integer#MAX_VALUE}.
     * This default implementation returns {@link Integer#MAX_VALUE} if
     * {@link #capacity64()} is greater than {@code Integer.MAX_VALUE}, and
     * returns {@code 0} if {@code capacity64()} is negative.
     *
     * @return the capacity clamped to the {@code int} range
     * @see #capacity64()
     */
    default int capacity() {
        final long result;
        return (result = this.capacity64()) > Integer.MAX_VALUE ? Integer.MAX_VALUE : (result < 0L ? 0 : (int) result);
    }

    /**
     * Returns the capacity of this filter.
     *
     * <p>Use this method instead of {@link #capacity()} when the count may exceed
     * {@link Integer#MAX_VALUE}.
     *
     * @return the capacity, typically expressed in bits
     * @see Container#capacity64()
     */
    long capacity64();

    /**
     * Returns the remaining capacity of this filter.
     *
     * <p>This operation is not applicable to Bloom filters. This default
     * implementation always returns {@code -1} to indicate that the remaining
     * capacity is unknown.
     *
     * @return always {@code -1}
     */
    default int remaining() {
        return -1;
    }

    /**
     * Returns the queue of hash behaviors used by this filter.
     *
     * <p>Neither the returned array nor any of its elements is {@code null}.
     * Callers must not modify the contents of the returned array.
     *
     * @return the hasher queue; the array and its elements are non-null, the
     *         {@code length} of array must non-zero
     */
    @NotNull
    HashBehavior<E>[] getHasherQueue();

    /**
     * Returns the number of bits currently set to 1 in this filter's internal
     * bit array, as an {@code int}.
     *
     * <p>This is a bits count, not the number of elements added and not an
     * estimate of the number of unique elements. Duplicate {@code add} calls do
     * not increase this count when all corresponding bits are already set.
     *
     * <p>The underlying count may exceed {@link Integer#MAX_VALUE}. This default
     * implementation returns {@link Integer#MAX_VALUE} if {@link #size64()} is
     * greater than {@code Integer.MAX_VALUE}, and returns {@code 0} if
     * {@code size64()} is negative.
     *
     * @return the number of bits currently set to 1, clamped to the {@code int}
     *         range
     * @see #size64()
     */
    default int size() {
        final long result;
        return (result = this.size64()) > Integer.MAX_VALUE ? Integer.MAX_VALUE : (result < 0L ? 0 : (int) result);
    }

    /**
     * Returns the number of bits currently set to 1 in this filter's internal
     * bit array, as a {@code long}.
     *
     * <p>This is a bits count, not the number of elements added and not an
     * estimate of the number of unique elements. Implementations with a
     * non-standard internal representation should document how this bit count is
     * defined.
     *
     * <p>Use this method instead of {@link #size()} when the count may exceed
     * {@link Integer#MAX_VALUE}.
     *
     * @return the number of bits currently set to 1
     * @see #size()
     */
    long size64();

    /**
     * Returns the current false-positive rate of this filter.
     *
     * <p>The false-positive rate is the probability that {@link #contains}
     * returns {@code true} for an element that was not added, that is, the
     * probability of a false positive.
     *
     * <p>The default implementation estimates the rate using the classic Bloom
     * filter formula
     * {@code (b / m)^k}, where {@code m} is the capacity in bits as returned by
     * {@link #capacity64()}, {@code b} is the number of bits currently set to 1
     * as returned by {@link #size64()}, and {@code k} is the number of hash
     * functions as given by the length of {@link #getHasherQueue()}.
     *
     * <p>This is equivalent to the usual form
     * {@code (1 - e^(-k*n/m))^k} when the expected number of set bits is
     * {@code b = m * (1 - e^(-k*n/m))}. Because {@link #size64()} now returns the
     * set-bit count rather than an estimated unique-element count, the default
     * implementation uses {@code b / m} directly.
     *
     * <p>This estimate reflects the current state of the filter and is therefore
     * recomputed on each call. Implementations that maintain the rate
     * incrementally, use a different construction, or track non-standard
     * semantics should override this method.
     *
     * @return the current false-positive rate in the range {@code [0.0, 1.0]},
     *         or {@link Double#NaN} if the rate is undefined for the current
     *         filter state
     */
    default double getCurrentErrors() {
        final long capacity = this.capacity64();
        if (capacity <= 0L) {
            return Double.NaN;
        }
        return Math.pow((double) this.size64() / (double) capacity, this.getHasherQueue().length);
    }

    /**
     * Returns an estimate of the number of unique elements that have been added
     * to this filter, as a {@code long}.
     *
     * <p>This value is estimated from the current number of bits set to 1, not
     * from an exact count of {@code add} calls. Because a Bloom filter cannot
     * reliably distinguish a duplicate from a new element, this value is an
     * <em>estimate</em> of the number of unique elements added. It may be smaller
     * than the actual number of {@code add} calls.
     *
     * <p>The default implementation assumes a classic Bloom filter and inverts the
     * expected set-bit ratio:
     * {@code n = -(m / k) * ln(1 - b / m)}, where {@code m} is the capacity in
     * bits as returned by {@link #capacity64()}, {@code b} is the number of bits
     * currently set to 1 as returned by {@link #size64()}, and {@code k} is the
     * number of hash functions as given by the length of
     * {@link #getHasherQueue()}.
     *
     * <p>If the current filter state is invalid or cannot be estimated, this
     * method returns {@link Long#MAX_VALUE}. In particular, this happens when
     * {@code capacity64() <= 0}, {@code size64() < 0}, {@code k <= 0}, or when
     * {@code size64() >= capacity64()}.
     *
     * <p>Implementations that maintain an exact or incremental estimate, use a
     * different construction, or track non-standard semantics should override
     * this method.
     *
     * @return the estimated number of unique added elements, or
     *         {@link Long#MAX_VALUE} if the estimate is undefined or saturated
     */
    default long getEstimatedElementCount() {
        final long capacity = this.capacity64();
        final long bits = this.size64();
        if (capacity <= 0L) {
            return Long.MAX_VALUE;
        }
        if (bits == 0L) {
            return 0L;
        }
        return Math.max(0L, Math.round(-((double) capacity / this.getHasherQueue().length) * Math.log1p(-bits / (double) capacity)));
    }

    /**
     * Unsupported. A {@code BloomFilter} is fixed-capacity and must not be
     * cleared to a new capacity.
     *
     * <p>This method is provided only to complete the clearing surface shared
     * with {@link Container}. It always throws
     * {@link UnsupportedOperationException} and must not be overridden with a
     * functional implementation. To remove all elements while keeping the
     * current capacity, use {@link Container#clear()}.
     *
     * @param newCapacity ignored
     * @throws UnsupportedOperationException always
     * @see Container#clear()
     */
    default void clear(int newCapacity) {
        throw new UnsupportedOperationException();
    }
}
