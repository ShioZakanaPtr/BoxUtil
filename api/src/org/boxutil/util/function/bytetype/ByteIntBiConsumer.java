package org.boxutil.util.function.bytetype;

import java.util.function.BiConsumer;

public interface ByteIntBiConsumer extends BiConsumer<Byte, Integer> {
    void accept(byte t, int u);

    default void accept(Byte t, Integer u) {
        this.accept(t == null ? (byte) 0 : t, u == null ? (int) 0 : u);
    }
}
