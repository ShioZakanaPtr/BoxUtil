package io.github.shiozakanaptr.acext.util.collection;

import io.github.shiozakanaptr.acext.api.collection.BloomFilter;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.JumboContainer;
import io.github.shiozakanaptr.acext.util.InternalCollectionUtil;
import io.github.shiozakanaptr.acext.util.function.objecttype.ObjToLongFunction;
import sun.misc.Unsafe;

import java.io.IOException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.locks.StampedLock;

/**
 * A thread-safe {@link BloomFilter} implementation backed by off-heap
 * (direct) memory and based 64-bit hash, intended for very large data sets.
 *
 * <p>This implementation stores the filter bits in off-heap (direct) memory
 * rather than on the Java heap. This makes it suitable for very large filters
 * whose bit storage would otherwise place significant pressure on the garbage
 * collector or exceed reasonable heap sizes.
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
 * <p><b>Memory release:</b> Because the backing memory is allocated off-heap,
 * it is not reclaimed by the garbage collector in the usual way. When an
 * instance is no longer needed, callers must invoke {@link Container#clear()}
 * to explicitly release the corresponding memory. Failing to do so may leak
 * off-heap memory for the lifetime of the process. Consult the documentation
 * of {@link Container#clear()} for any additional restrictions or
 * requirements that apply when releasing memory.
 *
 * <p><b>Thread safety:</b> This class is intended for concurrent use and is
 * thread-safe. The exact atomicity and consistency guarantees of individual
 * operations, including reads concurrent with {@code add} and
 * {@code clear()}. However, it was without segmented lock, for maximum
 * performance, recommend to batch {@code add} once before contains testing,
 * or less {@code add} calls during testing. The only exception is must <b>not</b>
 * call {@code clear()} when still have {@code contains} calls running. If
 * callers ensure not any {@code add} calls during testing, this implementation
 * exposed {@link #containsLockFree(Object)} for lock-free contains testing.
 *
 * <p>This class is {@link Cloneable} and {@link Serializable}. The corresponding
 * call will auto acquire a new memory for a new instance of when deserialization.
 *
 * @param <E> the type of elements tested by this filter
 *
 * @see BloomFilter
 * @see JumboContainer
 * @see JumboContainer#clear()
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
public class DirectConcurrentBloomFilter<E> implements BloomFilter<E>, JumboContainer, Cloneable, Serializable {
    private static final long serialVersionUID = -7142995032377883752L;
    public final static byte MAX_HASHER = 7;

    protected final static Unsafe UNSAFE;

    protected final static long BIT_PTR_FIELD;
    protected final static long LOCK_FIELD;

    static {
        try {
            final Class<?> fieldClass = Class.forName("java.lang.reflect.Field", false, Class.class.getClassLoader());
            final MethodHandle get = MethodHandles.lookup().findVirtual(fieldClass, "get", MethodType.methodType(Object.class, Object.class));
            final MethodHandle setAccessible = MethodHandles.lookup().findVirtual(fieldClass, "setAccessible", MethodType.methodType(Void.TYPE, boolean.class));
            final Object f = Unsafe.class.getDeclaredField("theUnsafe");
            setAccessible.invoke(f, true);
            UNSAFE = (Unsafe) get.invoke(f, null);

            BIT_PTR_FIELD = UNSAFE.objectFieldOffset(DirectConcurrentBloomFilter.class.getDeclaredField("bitPtr"));
            LOCK_FIELD = UNSAFE.objectFieldOffset(DirectConcurrentBloomFilter.class.getDeclaredField("lock"));
        } catch (Throwable e) {
            throw new ExceptionInInitializerError("Unsafe init failed: " + e.getMessage());
        }
    }

    protected final long posMask;
    protected transient long size = 0;
    protected transient volatile long bitPtr;
    protected final transient StampedLock lock = new StampedLock();
    protected final HashBehavior<E>[] hasherQueue;

    public static long getArrayLength(long n, double p, int k) {
        final long result = (long) Math.ceil(-(k * (double) Math.max(n, 1)) / Math.log1p(-Math.pow(p, 1.0d / k)) / 64.0);
        if (result < 0 || result > (1L << 50)) throw new IllegalArgumentException("Too many expected elements");
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

    @SafeVarargs
    public DirectConcurrentBloomFilter(long expectedElements, double expectedErrors, final HashBehavior<E>... hasherQueue) {
        checkErrors(expectedErrors);
        this.hasherQueue = checkHasherQueue(hasherQueue);
        final long length = getArrayLength(expectedElements, expectedErrors, hasherQueue.length), alloc = length << 3;
        this.posMask = length - 1;
        UNSAFE.setMemory(this.bitPtr = UNSAFE.allocateMemory(alloc), alloc, (byte) 0);
    }

    protected static long memGetLong(long ptr, long index) {
        return UNSAFE.getLong(ptr + (index << 3));
    }

    protected static long memGetLong(long ptr) {
        return UNSAFE.getLong(ptr);
    }

    protected static void memPutLong(long ptr, long value) {
        UNSAFE.putLong(ptr, value);
    }

    protected boolean in_add(final ObjToLongFunction<HashBehavior<E>> fun) {
        final long maskL = this.posMask;
        final HashBehavior<E>[] hasherL = this.hasherQueue;
        final HashBehavior<E> mixer = hasherL[0];
        final int posSeq = hasherL.length - 1;
        long p0 = 0L, p1 = 0L, p2 = 0L, p3 = 0L, p4 = 0L, p5 = 0L, p6 = 0L;
        long lastHash = 0;
        switch (posSeq) {
            case 6: p6 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[6])) & maskL;
            case 5: p5 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[5])) & maskL;
            case 4: p4 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[4])) & maskL;
            case 3: p3 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[3])) & maskL;
            case 2: p2 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[2])) & maskL;
            case 1: p1 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[1])) & maskL;
            case 0: p0 = mixer.mix64(lastHash, fun.apply(hasherL[0])) & maskL;
        }

        final StampedLock lockL = this.lock;
        long stamp = lockL.writeLock();
        final long ptrL;
        if ((ptrL = this.bitPtr) == 0) return false;
        boolean result = false;
        byte sizeInc = 0;
        switch (posSeq) {
            case 6: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p6 >>> 6) << 3))) & (bit = 1L << p6)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    sizeInc = 1;
                }
            }
            case 5: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p5 >>> 6) << 3))) & (bit = 1L << p5)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    ++sizeInc;
                }
            }
            case 4: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p4 >>> 6) << 3))) & (bit = 1L << p4)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    ++sizeInc;
                }
            }case 3: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p3 >>> 6) << 3))) & (bit = 1L << p3)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    ++sizeInc;
                }
            }case 2: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p2 >>> 6) << 3))) & (bit = 1L << p2)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    ++sizeInc;
                }
            }case 1: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p1 >>> 6) << 3))) & (bit = 1L << p1)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    ++sizeInc;
                }
            }case 0: {
                final long wordsPtr;
                final long bit, oldWord;
                if (((oldWord = memGetLong(wordsPtr = ptrL + ((p0 >>> 6) << 3))) & (bit = 1L << p0)) == 0) {
                    result = true;
                    memPutLong(wordsPtr, oldWord | bit);
                    ++sizeInc;
                }
            }
        }
        if (sizeInc != 0) this.size += sizeInc;
        lockL.unlockWrite(stamp);
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
        final long maskL = this.posMask;
        final HashBehavior<E>[] hasherL = this.hasherQueue;
        final HashBehavior<E> mixer = hasherL[0];
        final int posSeq = hasherL.length - 1;
        long p0 = 0L, p1 = 0L, p2 = 0L, p3 = 0L, p4 = 0L, p5 = 0L, p6 = 0L,
                v0 = 0L, v1 = 0L, v2 = 0L, v3 = 0L, v4 = 0L, v5 = 0L, v6 = 0L;
        long lastHash = 0;
        switch (posSeq) {
            case 6: p6 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[6])) & maskL;
            case 5: p5 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[5])) & maskL;
            case 4: p4 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[4])) & maskL;
            case 3: p3 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[3])) & maskL;
            case 2: p2 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[2])) & maskL;
            case 1: p1 = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[1])) & maskL;
            case 0: p0 = mixer.mix64(lastHash, fun.apply(hasherL[0])) & maskL;
        }

        final StampedLock lockL = this.lock;
        long stamp;
        boolean shouldLock = false;
        if ((stamp = lockL.tryOptimisticRead()) == 0) {
            stamp = lockL.readLock();
            shouldLock = true;
        }
        while (true) {
            final long ptrL;
            if ((ptrL = this.bitPtr) == 0) return false;
            switch (posSeq) {
                case 6: v6 = memGetLong(ptrL, p6 >>> 6);
                case 5: v5 = memGetLong(ptrL, p5 >>> 6);
                case 4: v4 = memGetLong(ptrL, p4 >>> 6);
                case 3: v3 = memGetLong(ptrL, p3 >>> 6);
                case 2: v2 = memGetLong(ptrL, p2 >>> 6);
                case 1: v1 = memGetLong(ptrL, p1 >>> 6);
                case 0: v0 = memGetLong(ptrL, p0 >>> 6);
            }
            if (shouldLock) {
                lockL.unlockRead(stamp);
                break;
            }
            if (lockL.validate(stamp)) break;
            stamp = lockL.readLock();
            shouldLock = true;
        }

        boolean result = true;
        switch (posSeq) {
            case 6: result = (v6 & (1L << p6)) != 0;
            case 5: result &= (v5 & (1L << p5)) != 0;
            case 4: result &= (v4 & (1L << p4)) != 0;
            case 3: result &= (v3 & (1L << p3)) != 0;
            case 2: result &= (v2 & (1L << p2)) != 0;
            case 1: result &= (v1 & (1L << p1)) != 0;
            case 0: result &= (v0 & (1L << p0)) != 0;
        }
        return result;
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

    protected boolean in_containsLockFree(final ObjToLongFunction<HashBehavior<E>> fun) {
        if (this.isEmpty()) return false;
        final HashBehavior<E>[] hasherL = this.hasherQueue;
        final HashBehavior<E> mixer = hasherL[0];
        final int posSeq = hasherL.length - 1;
        final long maskL = this.posMask;

        final long ptrL;
        if ((ptrL = this.bitPtr) == 0) return false;
        long lastHash = 0;
        switch (posSeq) {
            case 6: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[6])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
            case 5: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[5])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
            case 4: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[4])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
            case 3: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[3])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
            case 2: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[2])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
            case 1: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, lastHash = fun.apply(hasherL[1])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
            case 0: {
                final long pos;
                if ((memGetLong(ptrL, (pos = mixer.mix64(lastHash, fun.apply(hasherL[0])) & maskL) >>> 6) & (1L << pos)) == 0) return false;
            }
        }
        return true;
    }

    public boolean containsLockFree(int element) {
        return this.in_containsLockFree((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean containsLockFree(long element) {
        return this.in_containsLockFree((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean containsLockFree(float element) {
        return this.in_containsLockFree((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean containsLockFree(double element) {
        return this.in_containsLockFree((l_hasher) -> l_hasher.hashing64(element));
    }

    public boolean containsLockFree(Object element) {
        return this.in_containsLockFree((l_hasher) -> l_hasher.hashing64(element));
    }

    public long capacity64() {
        return (this.posMask + 1) << 6;
    }

    public HashBehavior<E>[] getHasherQueue() {
        return this.hasherQueue;
    }

    public long size64() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size < 1;
    }

    public void clear() {
        final StampedLock lockL = this.lock;
        final long stamp = lockL.writeLock();
        this.size = 0;
        final long ptr;
        if ((ptr = this.bitPtr) != 0 && UNSAFE.compareAndSwapLong(this, BIT_PTR_FIELD, ptr, 0)) {
            UNSAFE.freeMemory(ptr);
            this.bitPtr = 0;
        }
        lockL.unlockWrite(stamp);
    }

    public Object clone() {
        DirectConcurrentBloomFilter<E> result;
        try {
            result = (DirectConcurrentBloomFilter<E>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
        final long ptrL;
        if ((ptrL = this.bitPtr) != 0) {
            final long alloc = (this.posMask + 1) << 3;
            UNSAFE.copyMemory(ptrL, result.bitPtr = UNSAFE.allocateMemory(alloc), alloc);
        } else {
            result.size = 0;
            result.bitPtr = 0;
        }
        UNSAFE.putObject(result, LOCK_FIELD, new StampedLock());
        return result;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        final long ptrL;
        if ((ptrL = this.bitPtr) != 0) {
            final long alloc = (this.posMask + 1) << 3;
            for (int i = 0; i < alloc; i += 8) {
                out.writeLong(UNSAFE.getLong(ptrL + alloc));
            }
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
        if ((capacity & (capacity - 1)) != 0 || capacity < 0 || capacity > (1L << 50)) throw new StreamCorruptedException("Illegal capacity '" + capacity + '\'');
        UNSAFE.compareAndSwapObject(this, LOCK_FIELD, null, new StampedLock());
        final long alloc = (this.posMask + 1) << 3;
        final long oldPtr;
        if ((oldPtr = this.bitPtr) != 0) {
            UNSAFE.freeMemory(oldPtr);
        }
        final long ptrL;
        UNSAFE.setMemory(ptrL = this.bitPtr = UNSAFE.allocateMemory(alloc), alloc, (byte) 0);
        long sizeCount = 0;
        for (int i = 0; i < alloc; i += 8) {
            final long currSeg;
            sizeCount += Long.bitCount(currSeg = in.readLong());
            UNSAFE.putLong(ptrL + alloc, currSeg);
        }
        this.size = sizeCount;
    }
}
