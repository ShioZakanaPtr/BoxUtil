package io.github.shiozakanaptr.acext.util.collection;

import io.github.shiozakanaptr.acext.api.collection.BloomFilter;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjToLongFunction;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.io.IOException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;

/**
 * A {@link BloomFilter} implementation backed by an on-heap {@code long[]}
 * array and based 64-bit hash, intended for small data sets.
 *
 * <p>This implementation stores the filter bits in a {@code long[]} allocated
 * on the Java heap. Because the backing array is an ordinary Java object, it
 * becomes eligible for garbage collection once the filter is no longer
 * reachable; no explicit release step is required. Use this implementation
 * when the expected number of elements is small and the resulting bit storage
 * fits comfortably on the heap.
 *
 * <p>For very large data sets, or when the filter must avoid occupying heap
 * space, consider {@link DirectConcurrentBloomFilter} instead.
 *
 * <p>The hash behaviors are visited in reverse order, from
 * {@code hashers.length - 1} down to {@code 0}. The first round starts with
 * {@code previousHash = 0}. for each given element works as follows:</p>
 *
 * <pre>
 * {@code
 * HashBehavior<E>[] hasherL = this.hasherQueue;
 * HashBehavior<E> mixer = hasherL[0];
 *
 * long previousHash = 0L;
 * for (int i = hasherL.length - 1; i >= 0; i--) {
 *     long currentHash = hasherL[i].hashing64(element);
 *     long mixedHash = mixer.mix64(previousHash, currentHash);
 *
 *     if (isBitAbsent(mixedHash)) {
 *         return false;
 *     }
 *     previousHash = currentHash;
 * }
 * }
 * </pre>
 *
 * <p>The final round does not need to update {@code previousHash}. If all
 * selected bits are set, this method returns {@code true}; this indicates that
 * the element may be present. A {@code false} result means that at least one
 * required bit is clear, so the element is definitely not present.</p>
 *
 * <p><b>Not thread-safe:</b> Concurrent access to an instance must be
 * synchronized externally, or each instance must be confined to a single thread.
 *
 * <p>This class is {@link Cloneable} and {@link Serializable}.
 *
 * @param <E> the type of elements tested by this filter
 *
 * @see BloomFilter
 * @see DirectConcurrentBloomFilter
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public class HeapBloomFilter<E> implements BloomFilter<E>, Cloneable, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;
    public final static byte MAX_HASHER = 7;

    protected final static Unsafe UNSAFE;

    protected final static long BIT_MARK_FIELD;

    static {
        try {
            final Class<?> fieldClass = Class.forName("java.lang.reflect.Field", false, Class.class.getClassLoader());
            final MethodHandle get = MethodHandles.lookup().findVirtual(fieldClass, "get", MethodType.methodType(Object.class, Object.class));
            final MethodHandle setAccessible = MethodHandles.lookup().findVirtual(fieldClass, "setAccessible", MethodType.methodType(Void.TYPE, boolean.class));
            final Object f = Unsafe.class.getDeclaredField("theUnsafe");
            setAccessible.invoke(f, true);
            UNSAFE = (Unsafe) get.invoke(f, null);

            BIT_MARK_FIELD = UNSAFE.objectFieldOffset(HeapBloomFilter.class.getDeclaredField("bitMark"));
        } catch (Throwable e) {
            throw new ExceptionInInitializerError("Unsafe init failed: " + e.getMessage());
        }
    }

    public static long getArrayLength(long n, double p, int k) {
        final long result = (long) Math.ceil(-(k * (double) Math.max(n, 1)) / Math.log1p(-Math.pow(p, 1.0d / k)));
        if (result < 0 || result > (1L << 36)) throw new IllegalArgumentException("Too many expected elements");
        if (result < 1) throw new ArithmeticException();
        return InternalCollectionUtil.getCapacityPOT(result);
    }

    @SafeVarargs
    protected static <T> HashBehavior<T>[] checkHasherQueue(final HashBehavior<T>... hasherQueue) {
        if (hasherQueue == null) throw new NullPointerException();
        if (hasherQueue.length < 1) throw new IllegalArgumentException("At least with one hash behavior");
        if (hasherQueue.length > MAX_HASHER) throw new IllegalArgumentException("Too many hash behavior '" + hasherQueue.length + "' over '" + MAX_HASHER + "'");
        for (HashBehavior<T> hasher : hasherQueue) {
            if (hasher == null) throw new NullPointerException();
        }
        return hasherQueue;
    }

    protected static void checkErrors(double expectedErrors) {
        if (!(expectedErrors > 0.0d && expectedErrors < 1.0d)) throw new IllegalArgumentException("Illegal expectedErrors '" + expectedErrors + "' out of range (0.0, 1.0)");
    }

    protected transient long size = 0;
    protected final long posMask;
    protected final transient long[] bitMark;
    protected final HashBehavior<E>[] hasherQueue;

    @SafeVarargs
    public HeapBloomFilter(long expectedElements, double expectedErrors, final HashBehavior<E>... hasherQueue) {
        checkErrors(expectedErrors);
        this.hasherQueue = checkHasherQueue(hasherQueue);
        final long capacity = getArrayLength(expectedElements, expectedErrors, hasherQueue.length);
        this.posMask = capacity - 1;
        this.bitMark = new long[(int) (capacity >>> 6)];
    }

    protected boolean in_add(final ObjToLongFunction<HashBehavior<E>> fun) {
        if (this.isEmpty()) return false;
        final long[] bitL = this.bitMark;
        final HashBehavior<E>[] hasherL = this.hasherQueue;
        final HashBehavior<E> mixer = hasherL[0];
        final int posSeq = hasherL.length - 1;
        final long maskL = this.posMask;

        boolean result = false;
        byte sizeInc = 0;
        long lastHash = 0;
        switch (posSeq) {
            case 6: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[6])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    sizeInc = 1;
                }
            }
            case 5: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[5])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    ++sizeInc;
                }
            }
            case 4: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[4])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    ++sizeInc;
                }
            }
            case 3: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[3])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    ++sizeInc;
                }
            }
            case 2: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[2])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    ++sizeInc;
                }
            }
            case 1: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[1])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    ++sizeInc;
                }
            }
            case 0: {
                final long pos;
                final int words = (int) (pos = mixer.mix64(lastHash, fun.apply(hasherL[0])) & maskL) >>> 6;
                final long bit;
                if ((bitL[words] & (bit = 1L << pos)) == 0) {
                    result = true;
                    bitL[words] |= bit;
                    ++sizeInc;
                }
            }
        }
        if (sizeInc != 0) this.size += sizeInc;
        return result;
    }

    public boolean add(int element) {
        return this.in_add((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean add(long element) {
        return this.in_add((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean add(float element) {
        return this.in_add((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean add(double element) {
        return this.in_add((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean add(E element) {
        return this.in_add((l_hasher) -> l_hasher.hashing64(element));
    }

    protected boolean in_contains(final ObjToLongFunction<HashBehavior<E>> fun) {
        if (this.isEmpty()) return false;
        final long[] bitL = this.bitMark;
        final HashBehavior<E>[] hasherL = this.hasherQueue;
        final HashBehavior<E> mixer = hasherL[0];
        final int posSeq = hasherL.length - 1;
        final long maskL = this.posMask;

        long lastHash = 0;
        switch (posSeq) {
            case 6: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[6])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
            case 5: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[5])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
            case 4: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[4])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
            case 3: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[3])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
            case 2: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[2])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
            case 1: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[1])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
            case 0: {
                final long pos;
                if ((bitL[(int) (pos = mixer.mix64(lastHash, fun.apply(hasherL[0])) & maskL) >>> 6] & (1L << pos)) == 0) return false;
            }
        }
        return true;
    }

    public boolean contains(int element) {
        return this.in_contains((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean contains(long element) {
        return this.in_contains((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean contains(float element) {
        return this.in_contains((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean contains(double element) {
        return this.in_contains((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean contains(Object element) {
        return this.in_contains((l_hasher) -> l_hasher.hashing64(element));
    }

    public long capacity64() {
        return this.posMask + 1;
    }

    public @NotNull HashBehavior<E>[] getHasherQueue() {
        return this.hasherQueue;
    }

    public long size64() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public void clear() {
        this.size = 0;
        Arrays.fill(this.bitMark, 0L);
    }

    public Object clone() {
        HeapBloomFilter<E> result;
        try {
            result = (HeapBloomFilter<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        UNSAFE.putObject(result, BIT_MARK_FIELD, this.bitMark.clone());
        return this;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        for (long segment : this.bitMark) {
            out.writeLong(segment);
        }
    }

    @SafeVarargs
    protected static <T> void ioCheckHasherQueue(final HashBehavior<T>... hasherQueue) throws StreamCorruptedException {
        if (hasherQueue == null) throw new StreamCorruptedException("Illegal hash behavior 'NULL'");
        if (hasherQueue.length < 1) throw new StreamCorruptedException("At least with one hash behavior");
        if (hasherQueue.length > MAX_HASHER) throw new StreamCorruptedException("Too many hash behavior '" + hasherQueue.length + "' over '" + MAX_HASHER + "'");
        for (HashBehavior<T> hasher : hasherQueue) {
            if (hasher == null) throw new StreamCorruptedException("Illegal hash behavior 'NULL'");
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        ioCheckHasherQueue(this.hasherQueue);
        final long capacity = this.posMask + 1;
        if ((capacity & (capacity - 1)) != 0 || capacity < 0 || capacity > (1L << 36)) throw new StreamCorruptedException("Illegal capacity '" + capacity + '\'');

        final long[] bitL = new long[(int) (capacity >>> 6)];
        final int limit = bitL.length;
        long sizeCount = 0;
        for (int i = 0; i < limit;) {
            sizeCount += Long.bitCount(bitL[i++] = in.readLong());
        }
        UNSAFE.putObject(this, BIT_MARK_FIELD, bitL);
        this.size = sizeCount;
    }
}
