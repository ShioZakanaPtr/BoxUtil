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
            throw new IllegalArgumentException("Empty glyph set.");
        } else {
            final int[] glyphIdxL;
            final long[] glyphStorageL;
            this.glyphIdx = glyphIdxL = new int[maxCharValue + 1]; // => index + 1
            this.glyphStorage = glyphStorageL = new long[this.totalGlyph * dataLongStride];
            Arrays.fill(glyphIdxL, -1);

            int offset = 0;
            for (G glyph : src) {
                glyphIdxL[glyph.character() & 0xffff] = offset;
                glyph.store(offset, glyphStorageL);
                offset += dataLongStride;
            }
        }
    }

    public boolean get(char key, final G result) {
        final int[] glyphIdxL = this.glyphIdx;
        final int keyCast;
        if ((keyCast = key & 0xffff) >= glyphIdxL.length) return false;
        final int i;
        if ((i = glyphIdxL[keyCast]) < 0) return false;
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
