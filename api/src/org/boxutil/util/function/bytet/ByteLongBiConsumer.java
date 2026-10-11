package org.boxutil.util.function.bytet;

import java.util.function.BiConsumer;

public interface ByteLongBiConsumer extends BiConsumer<Byte, Long> {
    void accept(byte t, long u);

    default void accept(Byte t, Long u) {
        this.accept(t == null ? (byte) 0 : t, u == null ? (long) 0 : u);
    }
}
