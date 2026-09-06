package org.boxutil.backends.util;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

public class BUtil_GlyphSet<G extends BUtil_Glyph> {
    protected final int totalGlyph;
    protected final int[] glyphIdx;
    protected final long[] glyphStorage;

    public BUtil_GlyphSet(@NotNull final Set<G> src, final int maxCharValue, final byte dataLongStride) {
        this.totalGlyph = src.size();
        if (this.totalGlyph < 1) {
            this.glyphIdx = null;
            this.glyphStorage = null;
        } else {
            this.glyphIdx = new int[maxCharValue + 1]; // => index + 1
            this.glyphStorage = new long[this.totalGlyph * dataLongStride];
            Arrays.fill(this.glyphIdx, -1);

            int charPos, offset = 0;
            for (G glyph : src) {
                charPos = glyph.character() & 0xffff;
                this.glyphIdx[charPos] = offset;
                glyph.store(offset, this.glyphStorage);
                offset += dataLongStride;
            }
        }
    }

    public boolean get(char key, final G result) {
        final int keyCast = key & 0xffff;
        if (this.glyphIdx == null || keyCast > this.glyphIdx.length) return false;
        final int i = this.glyphIdx[keyCast];
        if (i < 0) return false;
        result.fetch(i, this.glyphStorage);
        return true;
    }

    public boolean contains(char key) {
        return this.glyphIdx != null && this.glyphIdx[key & 0xffff] > -1;
    }

    public int size() {
        return this.totalGlyph;
    }
}
