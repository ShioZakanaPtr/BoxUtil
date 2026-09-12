package org.boxutil.backends.util;

import org.boxutil.define.BoxEnum;
import org.boxutil.util.CalculateUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class BUtil_GlyphKerningMap {
    protected final int capacity;
    protected final int size;
    protected final int posMask;
    protected final byte[] state;
    protected final byte[] val;
    protected final int[] key;

    /**
     * @param loadFactor 0.7~0.8
     */
    public BUtil_GlyphKerningMap(@NotNull final Map<Integer, Byte> src, float loadFactor) {
        final int totalKerning = src.size();
        if (totalKerning < 2) {
            this.size = totalKerning;
            this.capacity = 2;
            this.posMask = 1;
            this.state = new byte[2];
            this.val = new byte[2];
            this.key = new int[2];
            this.state[0] = this.state[1] = BoxEnum.NEG_ONE;
            this.key[0] = this.key[1] = -1;
            if (totalKerning != 0)  {
                final var entry = src.entrySet().iterator().next();
                final int pos = hashing_JDK_HashMap(entry.getKey()) & this.posMask;
                this.state[pos] = 0;
                this.key[pos] = entry.getKey();
                this.val[pos] = entry.getValue();
            }
        } else {
            final int potCap = CalculateUtil.getPOTMax(totalKerning);
            this.size = totalKerning;
            this.capacity = ((float) this.size / (float) potCap) > Math.max(Math.min(loadFactor, 0.8f), 0.2f) ? (potCap << 1) : potCap;
            this.posMask = this.capacity - 1;

            this.state = new byte[this.capacity];
            this.val = new byte[this.capacity];
            this.key = new int[this.capacity];
            Arrays.fill(this.state, BoxEnum.NEG_ONE);
            Arrays.fill(this.key, -1);

            src.forEach(this::init_put);
        }
    }

    private void init_put(int key, byte value) {
        final byte[] state = this.state;
        final byte[] valL = this.val;
        final int[] keyL = this.key;
        int pos, currKey;
        if ((currKey = keyL[pos = hashing_JDK_HashMap(key) & this.posMask]) == -1) {
            state[pos] = 0;
            keyL[pos] = key;
            valL[pos] = value;
            return;
        }

        byte currPsl = 0, currState;
        do {
            if (currKey == key) {
                valL[pos] = value;
                return;
            }
            if (currPsl > (currState = state[pos]) && currState > -1) {
                final byte tmpState = state[pos], tmpValue = valL[pos];

                state[pos] = currPsl;
                currPsl = tmpState;
                keyL[pos] = key;
                key = currKey;
                valL[pos] = value;
                value = tmpValue;
            }
            if (++currPsl < -1) {
                return; // got next psl and check => -1 bug, discard it
            }
        } while ((currKey = keyL[pos = (pos + 1) & this.posMask]) != -1);

        state[pos] = (byte) (currPsl - 1); // 126 max
        valL[pos] = value;
        keyL[pos] = key;
    }

    public byte get(char first, char second) {
        final int key = fetchKey(first, second);

        final int[] keyL = this.key;
        int pos, currKey;
        if ((currKey = keyL[pos = hashing_JDK_HashMap(key) & this.posMask]) == -1) return 0;
        if (key == currKey) return this.val[pos];

        final byte[] state = this.state;
        byte currPsl = 0;
        while (true) {
            if ((currKey = keyL[pos = (pos + 1) & this.posMask]) == -1) return 0;
            if (key == currKey) return this.val[pos];
            if (++currPsl < -1 || state[pos] < currPsl) return 0;
        }
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

    public static int hashing_JDK_HashMap(int key) {
        return key ^ (key >>> 16);
    }
}
