package io.github.shiozakanaptr.acext.util;

import io.github.shiozakanaptr.acext.api.collection.Container;
import io.github.shiozakanaptr.acext.api.collection.HashBehavior;
import io.github.shiozakanaptr.acext.api.collection.HashContainer;

import java.io.StreamCorruptedException;
import java.util.Objects;

/**
 * An internal utility class providing shared helpers for collection
 * implementation classes in this project.
 *
 * <p>This class contains low-level support routines used by the concrete
 * collection implementations, including capacity computation, argument and
 * state validation, and standardized exception throwing. These helpers exist to
 * keep the implementation classes consistent and to avoid duplicating such
 * logic across them.
 *
 * <p><strong>This class is not part of the public API.</strong> It is intended
 * solely for use by collection implementation classes within this project.
 * Ordinary developers should not call, extend, or otherwise depend on the
 * members of this class, as its contents may change or be removed without
 * notice. For this reason, individual methods are not documented.
 *
 * <p>All members of this class are static. This class cannot be instantiated.
 * @author ShioZakanaPtr
 * @version 1.0.0
 */
public final class InternalCollectionUtil {
    public static void throwCapacityOverflowTips() {
        throw new IllegalStateException("The capacity of container overflow; should split to different ones");
    }

    public static void checkCapacity(long cap, long max) {
        if (!(cap >= 0 && cap <= max)) throw new IllegalArgumentException("Capacity overflow '" + cap + "' over '" + max + '\'');
    }

    public static void checkCapacity(long cap) {
        checkCapacity(cap, Container.MAXIMUM_CAPACITY);
    }

    public static void checkIndex(long index, long min, long max) {
        if (!(index >= min && index <= max)) throw new IndexOutOfBoundsException("Illegal index '" + index + "' out of range [" + min + ',' + max + ']');
    }

    public static void checkBound(long index, long min, long max) {
        if (!(index >= min && index < max)) throw new IndexOutOfBoundsException("Illegal index '" + index + "' out of range [" + min + ',' + max + ')');
    }

    public static void checkRange(long fromIndex, long toIndex, long fromLimit, long toLimit) {
        if (fromIndex < fromLimit) throw new IndexOutOfBoundsException("Out of bounds '" + fromIndex + '\'');
        if (toIndex > toLimit) throw new IndexOutOfBoundsException("Out of bounds '" + toIndex + '\'');
        if (fromIndex > toIndex) throw new IllegalArgumentException("Inverted index range [" + fromIndex + ',' + toIndex + ')');
    }

    public static int expandArray(int length) {
        int reqCap = length < 64 ? length << 1 : length + (length >>> 1) + 1;
        if (length < Container.MAXIMUM_CAPACITY) reqCap = Math.min(reqCap, Container.MAXIMUM_CAPACITY);
        else InternalCollectionUtil.throwCapacityOverflowTips();
        return reqCap;
    }

    public static int getCapacityPOT(int rawCap) {
        return 1 << (32 - Integer.numberOfLeadingZeros(rawCap - 1));
    }

    public static long getCapacityPOT(long rawCap) {
        return 1L << (64 - Long.numberOfLeadingZeros(rawCap - 1L));
    }

    public static int getHashTableCapacity(int elementSize, float loadFactor) {
        return (int) Math.max(checkHashCapacity(getCapacityPOT((long) Math.ceil(elementSize / (double) loadFactor))), 2);
    }

    public static int getHashTableMaxRemaining(int capacity, float loadFactor, boolean reservedNull) {
        final int remaining = (int) Math.floor(capacity * (double) loadFactor);
        if (!(remaining >= 0 && remaining <= HashContainer.MAXIMUM_CAPACITY)) throw new IllegalArgumentException("Remaining overflow '" + capacity + "', load factor '" + loadFactor + "' reservedNull '" + reservedNull + '\'');
        final int result = Math.max(remaining, 1);
        return reservedNull ? result + 1 : result;
    }

    public static int checkHashCapacity(int cap) {
        checkCapacity(cap, HashContainer.MAXIMUM_CAPACITY);
        return cap;
    }

    public static long checkHashCapacity(long cap) {
        checkCapacity(cap, HashContainer.MAXIMUM_CAPACITY);
        return cap;
    }

    public static float checkHashLoadFactor(float loadFactor, float min, float max) {
        if (!(Float.isFinite(loadFactor) && loadFactor >= min && loadFactor <= max)) throw new IllegalArgumentException("Illegal load factor '" + loadFactor + "' out of range [" + min + ',' + max + ']');
        return loadFactor;
    }

    public static int expandHashArray(int length) {
        final int reqCap;
        if (!((reqCap = length << 1) >= 0 && reqCap <= HashContainer.MAXIMUM_CAPACITY)) InternalCollectionUtil.throwCapacityOverflowTips();
        return reqCap;
    }

    public final static class SerialCheck {
        public static void ioCheckSize(long size, long min, long max) throws StreamCorruptedException {
            if (!(size >= min && size <= max)) throw new StreamCorruptedException("Illegal collection size expected [" + min + ',' + max + "] but actual '" + size + '\'');
        }

        public static void ioCheckElementCount(long currCount, long expectedCount) throws StreamCorruptedException {
            if (currCount > expectedCount) throw new StreamCorruptedException("Illegal elements count: expected '" + expectedCount + "' but actual '" + currCount + '\'');
        }

        public static void ioCheckElementIsDuplicate(Object target) throws StreamCorruptedException {
            throw new StreamCorruptedException("Duplicate element '" + target + '\'');
        }

        public static void ioCheckKeyIsDuplicate(Object target) throws StreamCorruptedException {
            throw new StreamCorruptedException("Duplicate key '" + target + '\'');
        }

        public static int ioGetHashTableCapacity(long elementSize, float loadFactor, long max) throws StreamCorruptedException {
            final long cap = getCapacityPOT((long) Math.ceil(elementSize / (double) loadFactor));
            if (!(cap >= 0 && cap <= max)) throw new StreamCorruptedException("Illegal capacity from size '" + elementSize + "' and load factor '" + loadFactor + "', over '" + max + '\'');
            return (int) Math.max(cap, 2);
        }

        public static void ioCheckHashBehavior(HashBehavior<?> behavior) throws StreamCorruptedException {
            if (behavior == null) throw new StreamCorruptedException("Illegal hash behavior 'NULL'");
        }

        public static void ioCheckHashLoadFactor(float loadFactor, float min, float max) throws StreamCorruptedException {
            if (!(Float.isFinite(loadFactor) && loadFactor >= min && loadFactor <= max)) throw new StreamCorruptedException("Illegal collection size expected [" + min + ',' + max + "] but actual '" + loadFactor + '\'');
        }

        private SerialCheck() {}
    }

    public static boolean arrayEquals(byte[] src, int srcFrom, int srcTo, byte[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(short[] src, int srcFrom, int srcTo, short[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(int[] src, int srcFrom, int srcTo, int[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(long[] src, int srcFrom, int srcTo, long[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(char[] src, int srcFrom, int srcTo, char[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(float[] src, int srcFrom, int srcTo, float[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(double[] src, int srcFrom, int srcTo, double[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (src[i++] != dst[j++]) return false;
        }
        return true;
    }

    public static boolean arrayEquals(Object[] src, int srcFrom, int srcTo, Object[] dst, int dstFrom, int dstTo) {
//        return Arrays.equals(src, srcFrom, srcTo, dst, dstFrom, dstTo); // JDK 14+
        if (src == dst) return true;
        if (src == null || dst == null) return false;
        if (srcTo - srcFrom != dstTo - dstFrom) return false;
        for (int i = srcFrom, j = dstFrom; i < srcTo;) {
            if (!Objects.equals(src[i++], dst[j++])) return false;
        }
        return true;
    }

    private InternalCollectionUtil() {}
}
