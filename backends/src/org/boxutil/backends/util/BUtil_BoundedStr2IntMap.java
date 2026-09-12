package org.boxutil.backends.util;

import org.boxutil.util.CalculateUtil;

import java.util.Arrays;

public class BUtil_BoundedStr2IntMap {
    protected final int capacity;
    protected final int posMask;
    protected int size = 0;
    protected final CharSequence[] key;
    protected final int[] val;

    /**
     * Any key must be not null.
     * Only for 100% hit.
     *
     * @param estimatedSize maximum allowed key
     * @param loadFactor 0.7~0.8
     */
    public BUtil_BoundedStr2IntMap(int estimatedSize, float loadFactor) {
        if (estimatedSize > (1 << 29) || estimatedSize < 1) throw new IllegalArgumentException("The capacity of map overflow");
        final int potCap = CalculateUtil.getPOTMax(estimatedSize);
        this.capacity = ((float) estimatedSize / (float) potCap) > Math.max(Math.min(loadFactor, 0.8f), 0.2f) ? (potCap << 1) : potCap;
        this.posMask = this.capacity - 1;

        this.key = new CharSequence[this.capacity];
        this.val = new int[this.capacity];
        Arrays.fill(this.key, null);
    }

    public boolean put(CharSequence key, int value) {
        if (this.size >= this.capacity) return false;

        final CharSequence[] keyL = this.key;
        CharSequence currKey;
        int pos;
        if ((currKey = keyL[pos = hashing_JDK_HashMap(key.hashCode()) & this.posMask]) == null) {
            keyL[pos] = key;
            this.val[pos] = value;
            this.size++;
            return true;
        }

        do {
            if (currKey.equals(key)) {
                this.val[pos] = value;
                return true;
            }
        } while ((currKey = keyL[pos = (pos + 1) & this.posMask]) != null);

        keyL[pos] = key;
        this.val[pos] = value;
        this.size++;
        return true;
    }

    public int getOrDefault(CharSequence key, int defaultValue) {
        final CharSequence[] keyL = this.key;
        CharSequence currKey;
        int pos;
        if ((currKey = keyL[pos = hashing_JDK_HashMap(key.hashCode()) & this.posMask]) == null) return defaultValue;
        if (key.equals(currKey)) return this.val[pos];

        while (true) {
            if ((currKey = keyL[pos = (pos + 1) & this.posMask]) == null) return defaultValue;
            if (key.equals(currKey)) return this.val[pos];
        }
    }

    public void clear() {
        Arrays.fill(this.key, null);
        this.size = 0;
    }

    public int capacity() {
        return this.capacity;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size() < 1;
    }

    public static int hashing_JDK_HashMap(int key) {
        return key ^ (key >>> 16);
    }
}
