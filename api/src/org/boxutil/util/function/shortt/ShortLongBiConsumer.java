package org.boxutil.util.function.shortt;

import java.util.function.BiConsumer;

public interface ShortLongBiConsumer extends BiConsumer<Short, Long> {
    void accept(short t, long u);

    default void accept(Short t, Long u) {
        this.accept(t == null ? (short) 0 : t, u == null ? (long) 0 : u);
    }
}
