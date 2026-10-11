package org.boxutil.util.function.bytet;

import java.util.function.BiConsumer;

public interface ByteShortBiConsumer extends BiConsumer<Byte, Short> {
    void accept(byte t, short u);

    default void accept(Byte t, Short u) {
        this.accept(t == null ? (byte) 0 : t, u == null ? (short) 0 : u);
    }
}
