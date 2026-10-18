package org.boxutil.util.function.longtype;

import java.util.function.BiConsumer;

public interface LongShortBiConsumer extends BiConsumer<Long, Short> {
    void accept(long t, short u);

    default void accept(Long t, Short u) {
        this.accept(t == null ? (long) 0 : t, u == null ? (short) 0 : u);
    }
}
