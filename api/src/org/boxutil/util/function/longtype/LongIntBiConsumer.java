package org.boxutil.util.function.longtype;

import java.util.function.BiConsumer;

public interface LongIntBiConsumer extends BiConsumer<Long, Integer> {
    void accept(long t, int u);

    default void accept(Long t, Integer u) {
        this.accept(t == null ? (long) 0 : t, u == null ? (int) 0 : u);
    }
}
