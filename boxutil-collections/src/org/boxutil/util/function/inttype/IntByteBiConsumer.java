package org.boxutil.util.function.inttype;

import java.util.function.BiConsumer;

public interface IntByteBiConsumer extends BiConsumer<Integer, Byte> {
    void accept(int t, byte u);

    default void accept(Integer t, Byte u) {
        this.accept(t == null ? (int) 0 : t, u == null ? (byte) 0 : u);
    }
}
