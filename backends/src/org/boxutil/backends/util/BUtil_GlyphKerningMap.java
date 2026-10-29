package org.boxutil.backends.util;

import org.boxutil.base.api.container.inttype.IntByteMap;
import org.boxutil.util.CollectionUtil;
import org.boxutil.util.HashingUtil;
import org.jetbrains.annotations.NotNull;

public class BUtil_GlyphKerningMap {
    protected final boolean withNullKey;
    protected final int size;
    protected final int posMask;
    protected final byte[] valueTable;
    protected final int[] keyTable;

    /**
     * @param loadFactor 0.7~0.8
     */
    public BUtil_GlyphKerningMap(@NotNull final IntByteMap src, float loadFactor, boolean withZeroKerning) {
        final int totalKerning = this.size = src.size();
        this.withNullKey = withZeroKerning;
        if (totalKerning < 1) {
            this.posMask = 0;
            this.valueTable = null;
            this.keyTable = null;
        } else {
            final int capacity = CollectionUtil.getHashTableCapacity(Math.max(totalKerning, 2), loadFactor);
            this.posMask = capacity - 1;

            this.valueTable = new byte[capacity + 1];
            this.keyTable = new int[capacity];
            src.forEach((int l_key, byte l_value) -> {
                if (l_key == 0) {
                    this.valueTable[capacity] = l_value;
                    return;
                }

                final int[] keyL = this.keyTable;
                int pos = HashingUtil.fibonacciBit32(l_key);
                for (int currKey; (currKey = keyL[pos &= this.posMask]) != 0; pos++) {
                    if (l_key == currKey) {
                        this.valueTable[pos] = l_value;
                        return;
                    }
                }
                keyL[pos] = l_key;
                this.valueTable[pos] = l_value;
            });
        }
    }

    public byte get(char first, char second) {
        if (this.size < 1) return 0;
        final int key = fetchKey(first, second);
        if (key == 0) return this.withNullKey ? this.valueTable[this.valueTable.length - 1] : 0;

        int currKey;
        for (int pos = HashingUtil.fibonacciBit32(key); (currKey = this.keyTable[pos &= this.posMask]) != 0; pos++) {
            if (key == currKey) return this.valueTable[pos];
        }
        return 0;
    }

    public int size() {
        return this.size;
    }

    public static int fetchKey(char first, char second) {
        return (first & 0xffff) << 16 | (second & 0xffff);
    }
}
