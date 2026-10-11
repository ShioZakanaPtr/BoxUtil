package org.boxutil.util.function.bytet;

import java.util.function.BiConsumer;

public interface ByteByteBiConsumer extends BiConsumer<Byte, Byte> {
    void accept(byte t, byte u);

    default void accept(Byte t, Byte u) {
        this.accept(t == null ? (byte) 0 : t, u == null ? (byte) 0 : u);
    }
}
