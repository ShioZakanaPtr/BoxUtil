package org.boxutil.util.function.inttype;

import java.util.function.BiConsumer;

public interface IntLongBiConsumer extends BiConsumer<Integer, Long> {
    void accept(int t, long u);

    default void accept(Integer t, Long u) {
        this.accept(t == null ? (int) 0 : t, u == null ? (long) 0 : u);
    }
}
