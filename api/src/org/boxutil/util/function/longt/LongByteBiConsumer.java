package org.boxutil.util.function.longt;

import java.util.function.BiConsumer;

public interface LongByteBiConsumer extends BiConsumer<Long, Byte> {
    void accept(long t, byte u);

    default void accept(Long t, Byte u) {
        this.accept(t == null ? (long) 0 : t, u == null ? (byte) 0 : u);
    }
}
