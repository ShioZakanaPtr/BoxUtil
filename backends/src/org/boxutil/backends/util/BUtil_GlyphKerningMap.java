package org.boxutil.backends.util;

import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class BUtil_GlyphKerningMap {
    private final byte fixedValue;
    private final int capacity;
    private final int size;
    private final int posMask;
    private final int fixedKey;
    private final byte[] state; // -1 => empty, (x > -1) => psl
    private final byte[] values;
    private final int[] keys;

    /**
     * @param loadFactor 0.7~0.8
     */
    public BUtil_GlyphKerningMap(@NotNull final Map<Integer, Byte> src, float loadFactor) {
        final int totalKerning = src.size();
        if (totalKerning < 2) {
            this.size = totalKerning;
            this.capacity = totalKerning;
            this.posMask = 1;
            this.state = null;
            this.values = null;
            this.keys = null;
            if (totalKerning == 0) {
                this.fixedValue = 0;
                this.fixedKey = 0;
            } else {
                final var entry = src.entrySet().iterator().next();
                this.fixedValue = entry.getValue();
                this.fixedKey = entry.getKey();
            }
        } else {
            this.fixedValue = 0;
            this.fixedKey = 0;
            final int potCap = CalculateUtil.getPOTMax(totalKerning);
            this.size = totalKerning;
            this.capacity = ((float) this.size / (float) potCap > loadFactor) ? (potCap << 1) : potCap;
            this.posMask = this.capacity - 1;

            this.state = new byte[this.capacity];
            this.values = new byte[this.capacity];
            this.keys = new int[this.capacity];
            Arrays.fill(this.state, (byte) -1);

            src.forEach(this::init_put);
        }
    }

    private void init_put(int key, byte value) {
        int pos = hasher(key) & this.posMask;
        byte currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (currPsl > currState) {
                final int tmpKey = this.keys[pos];
                final byte tmpValue = this.values[pos],
                        tmpState = this.state[pos];

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
            if (currPsl < -1 || pos == startPos) return;
        }

        this.state[pos] = currPsl;
        this.keys[pos] = key;
        this.values[pos] = value;
    }

    public byte get(char first, char second) {
        final int key = fetchKey(first, second);
        if (this.size < 2) return key == this.fixedKey ? this.fixedValue : 0;

        int pos = hasher(fetchKey(first, second)) & this.posMask;
        byte currPsl = 0, currState;

        final int startPos = pos;
        while ((currState = this.state[pos]) > -1) {
            if (this.keys[pos] == key) return this.values[pos];
            if (currState < currPsl) return 0;
            pos++;
            pos &= this.posMask;
            currPsl++;
            if (currPsl < -1 || pos == startPos) return 0;
        }
        return 0;
    }

    public int capacity() {
        return this.capacity;
    }

    public int size() {
        return this.size;
    }

    public static int fetchKey(char first, char second) {
        return (first & 0xffff) << 16 | (second & 0xffff);
    }

    public static int hasher(int key) {
        int h = key;
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }
}
