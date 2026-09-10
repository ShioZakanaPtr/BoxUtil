package org.boxutil.backends.util;

import org.boxutil.util.CalculateUtil;
import org.boxutil.util.container.ContainerHasher;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class BUtil_BoundedIntMap<K> {
    protected final int capacity;
    protected final int posMask;
    protected int size = 0;
    protected int fixedValue;
    protected K fixedKey;
    protected final int[] svs; // i = {iState, iValue}, state = {-1 => empty, (x > -1) => psl}
    protected final K[] keys;

    /**
     * @param loadFactor 0.7~0.8
     */
    public BUtil_BoundedIntMap(int storageSize, float loadFactor) {
        if (storageSize > (1 << 29)) throw new IllegalArgumentException("The capacity of map overflow");
        if (storageSize < 2) {
            this.capacity = storageSize;
            this.posMask = 1;
            this.svs = null;
            this.keys = null;
        } else {
            this.fixedValue = 0;
            this.fixedKey = null;
            final int potCap = CalculateUtil.getPOTMax(storageSize);
            this.capacity = ((float) storageSize / (float) potCap > loadFactor) ? (potCap << 1) : potCap;
            this.posMask = this.capacity - 1;

            this.svs = new int[this.capacity << 1];
            this.keys = (K[]) new Object[this.capacity];
            Arrays.fill(this.svs, (byte) -1);
        }
    }

    public boolean put(K key, int value) {
        if (key == null || this.size >= this.capacity) return false;
        if (this.capacity < 2) {
            if (key.equals(this.fixedKey)) {
                this.fixedValue = value;
                this.size = 1;
            } else if (this.fixedKey == null) {
                this.fixedKey = key;
                this.fixedValue = value;
                this.size = 1;
            } else return false;
            return true;
        }

        int pos = ContainerHasher.hashing_Directly(key.hashCode()) & this.posMask,
                realPos = pos << 1,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.svs[realPos]) > -1) {
            if (key.equals(this.keys[pos])) {
                this.svs[realPos + 1] = value;
                return true;
            }
            if (currPsl > currState) {
                final int realValPos = realPos + 1,
                        tmpState = this.svs[realPos],
                        tmpValue = this.svs[realValPos];
                final K tmpKey = this.keys[pos];

                this.keys[pos] = key;
                key = tmpKey;
                this.svs[realPos] = value;
                value = tmpValue;
                this.svs[realValPos] = currPsl;
                currPsl = tmpState;
            }
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return false;
        }

        this.svs[realPos] = currPsl;
        this.keys[pos] = key;
        this.svs[realPos + 1] = value;
        this.size++;
        return true;
    }

    public int getOrDefault(K key, int defaultValue) {
        if (key == null || this.isEmpty()) return defaultValue;
        if (this.capacity < 2) return key.equals(this.fixedKey) ? this.fixedValue : defaultValue;

        int pos = ContainerHasher.hashing_Directly(key.hashCode()) & this.posMask,
                realPos = pos << 1,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.svs[realPos]) > -1) {
            if (key.equals(this.keys[pos])) return this.svs[realPos + 1];
            if (currState < currPsl) return defaultValue;
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return defaultValue;
        }
        return defaultValue;
    }

    protected void removeAndShift(int currPos, int currRealPos) {
        this.size--;
        this.svs[currRealPos] = -1;
        this.keys[currPos] = null;
        for (int i = (currPos + 1) & this.posMask; i != currPos; i = ++i & this.posMask) {
            final int realPos = i << 1;
            if (this.svs[realPos] < 1) return;
            final int prePos = (i - 1) & this.posMask,
                    preRealPos = prePos << 1;
            this.svs[preRealPos] = --this.svs[realPos];
            this.keys[prePos] = this.keys[i];
            this.svs[preRealPos + 1] = this.svs[realPos + 1];
            this.svs[i] = -1;
            this.keys[realPos] = null;
        }
    }

    public boolean remove(K key) {
        if (key == null || this.isEmpty()) return false;
        if (this.capacity < 2) {
            if (key.equals(this.fixedKey)) {
                this.fixedKey = null;
                this.size = 0;
                return true;
            } else return false;
        }

        int pos = ContainerHasher.hashing_Directly(key.hashCode()) & this.posMask,
                realPos = pos << 1,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.svs[realPos]) > -1) {
            if (key.equals(this.keys[pos])) {
                this.removeAndShift(pos, realPos);
                return true;
            }
            if (currState < currPsl) return false;
            pos = ++pos & this.posMask;
            realPos = pos << 1;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return false;
        }
        return false;
    }

    public void clear() {
        if (this.capacity < 2) {
            this.fixedKey = null;
        } else {
            Arrays.fill(this.svs, (byte) -1);
            Arrays.fill(this.keys, null);
        }
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
}
