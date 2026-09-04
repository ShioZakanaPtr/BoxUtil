package org.boxutil.backends.util;

public interface BUtil_Glyph {
    char character();

    void fetch(int offset, final long[] raw);

    void store(int offset, final long[] raw);
}
