package org.boxutil.util.function.shortt;

import java.util.function.BiConsumer;

public interface ShortByteBiConsumer extends BiConsumer<Short, Byte> {
    void accept(short t, byte u);

    default void accept(Short t, Byte u) {
        this.accept(t == null ? (short) 0 : t, u == null ? (byte) 0 : u);
    }
}
