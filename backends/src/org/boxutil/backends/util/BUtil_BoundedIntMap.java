package org.boxutil.backends.util;

import org.boxutil.util.CalculateUtil;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class BUtil_BoundedIntMap<K> {
    private final int capacity;
    private final int posMask;
    private int size = 0;
    private int fixedValue;
    private K fixedKey;
    private final int[] state; // -1 => empty, (x > -1) => psl
    private final int[] values;
    private final K[] keys;

    /**
     * @param loadFactor 0.7~0.8
     */
    public BUtil_BoundedIntMap(int storageSize, float loadFactor) {
        if (storageSize < 2) {
            this.capacity = storageSize;
            this.posMask = 1;
            this.state = null;
            this.values = null;
            this.keys = null;
        } else {
            this.fixedValue = 0;
            this.fixedKey = null;
            final int potCap = CalculateUtil.getPOTMax(storageSize);
            this.capacity = ((float) storageSize / (float) potCap > loadFactor) ? (potCap << 1) : potCap;
            this.posMask = this.capacity - 1;

            this.state = new int[this.capacity];
            this.values = new int[this.capacity];
            this.keys = (K[]) new Object[this.capacity];
            Arrays.fill(this.state, (byte) -1);
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

        int pos = key.hashCode() & this.posMask,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (key.equals(this.keys[pos])) {
                this.values[pos] = value;
                return true;
            }
            if (currPsl > currState) {
                final int tmpState = this.state[pos],
                        tmpValue = this.values[pos];
                final K tmpKey = this.keys[pos];

                this.keys[pos] = key;
                key = tmpKey;
                this.values[pos] = value;
                value = tmpValue;
                this.state[pos] = currPsl;
                currPsl = tmpState;
            }
            pos++;
            pos &= this.posMask;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return false;
        }

        this.state[pos] = currPsl;
        this.keys[pos] = key;
        this.values[pos] = value;
        this.size++;
        return true;
    }

    public int getOrDefault(K key, int defaultValue) {
        if (key == null || this.isEmpty()) return defaultValue;
        if (this.capacity < 2) return key.equals(this.fixedKey) ? this.fixedValue : defaultValue;

        int pos = key.hashCode() & this.posMask,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (key.equals(this.keys[pos])) return this.values[pos];
            if (currState < currPsl) return defaultValue;
            pos++;
            pos &= this.posMask;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return defaultValue;
        }
        return defaultValue;
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

        int pos = key.hashCode() & this.posMask,
                currPsl = 0,
                currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (key.equals(this.keys[pos])) {
                this.state[pos] = -1;
                this.keys[pos] = null;
                this.size--;
                return true;
            }
            if (currState < currPsl) return false;
            pos++;
            pos &= this.posMask;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return false;
        }
        return false;
    }

    public void clear() {
        if (this.capacity < 2) {
            this.fixedKey = null;
        } else {
            Arrays.fill(this.state, (byte) -1);
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
