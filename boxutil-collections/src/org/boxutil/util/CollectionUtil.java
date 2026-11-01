package org.boxutil.util;

import org.boxutil.base.api.container.Container;
import org.boxutil.base.api.container.HashBehavior;
import org.boxutil.base.api.container.HashContainer;

import java.io.StreamCorruptedException;

public final class CollectionUtil {
    public static void throwCapacityOverflowTips() {
        throw new IllegalStateException("The capacity of container overflow; should split to different ones");
    }

    public static void checkCapacity(long cap) {
        if (!(cap >= 0 && cap <= Container.MAXIMUM_CAPACITY)) throw new IllegalArgumentException("Capacity overflow " + cap);
    }

    public static void checkIndex(long index, long min, long max) {
        if (!(index >= min && index <= max)) throw new IndexOutOfBoundsException("Illegal index '" + index + "' out of range [" + min + ',' + max + ')');
    }

    public static void checkRange(long fromIndex, long toIndex, long fromLimit, long toLimit) {
        if (fromIndex < fromLimit) throw new IndexOutOfBoundsException("Out of bounds '" + fromIndex + '\'');
        if (toIndex > toLimit) throw new IndexOutOfBoundsException("Out of bounds '" + toIndex + '\'');
        if (fromIndex > toIndex) throw new IllegalArgumentException("Inverted index range [" + fromIndex + ',' + toIndex + ')');
    }

    public static int expandArray(int length) {
        int reqCap = length < 64 ? length << 1 : length + (length >>> 1) + 1;
        if (length < Container.MAXIMUM_CAPACITY) reqCap = Math.min(reqCap, Container.MAXIMUM_CAPACITY);
        else CollectionUtil.throwCapacityOverflowTips();
        return reqCap;
    }

    public static int getCapacityPOT(int rawCap) {
        return 1 << (32 - Integer.numberOfLeadingZeros(rawCap - 1));
    }

    public static long getCapacityPOT(long rawCap) {
        return 1L << (64 - Long.numberOfLeadingZeros(rawCap - 1L));
    }

    public static int getHashTableCapacity(int elementSize, float loadFactor) {
        final long cap;
        checkHashCapacity(cap = getCapacityPOT((long) Math.ceil(elementSize / (double) loadFactor)));
        return (int) Math.max(cap, 2);
    }

    public static int getHashTableMaxRemaining(int capacity, float loadFactor, boolean reservedNull) {
        final int remaining = (int) Math.floor(capacity * (double) loadFactor);
        if (!(remaining >= 0 && remaining <= HashContainer.MAXIMUM_CAPACITY)) throw new IllegalArgumentException("Remaining overflow '" + capacity + "', load factor '" + loadFactor + "' reservedNull '" + reservedNull + '\'');
        final int result = Math.max(remaining, 1);
        return reservedNull ? result + 1 : result;
    }

    public static void checkHashCapacity(int cap) {
        if (!(cap >= 0 && cap <= HashContainer.MAXIMUM_CAPACITY)) throw new IllegalArgumentException("Capacity overflow " + cap);
    }

    public static void checkHashCapacity(long cap) {
        if (!(cap >= 0 && cap <= HashContainer.MAXIMUM_CAPACITY)) throw new IllegalArgumentException("Capacity overflow " + cap);
    }

    public static void checkHashLoadFactor(float loadFactor, float min, float max) {
        if (!(Float.isFinite(loadFactor) && loadFactor >= min && loadFactor <= max)) throw new IllegalArgumentException("Illegal load factor '" + loadFactor + "' out of range [" + min + ',' + max + ']');
    }

    public static int expandHashArray(int length) {
        final int reqCap;
        if (!((reqCap = length << 1) >= 0 && reqCap <= HashContainer.MAXIMUM_CAPACITY)) CollectionUtil.throwCapacityOverflowTips();
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

    private CollectionUtil() {}
}
