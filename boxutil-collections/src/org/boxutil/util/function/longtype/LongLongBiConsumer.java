package org.boxutil.util.function.longtype;

import java.util.function.BiConsumer;

public interface LongLongBiConsumer extends BiConsumer<Long, Long> {
    void accept(long t, long u);

    default void accept(Long t, Long u) {
        this.accept(t == null ? (long) 0 : t, u == null ? (long) 0 : u);
    }
}
