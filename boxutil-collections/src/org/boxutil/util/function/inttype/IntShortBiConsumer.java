package org.boxutil.util.function.inttype;

import java.util.function.BiConsumer;

public interface IntShortBiConsumer extends BiConsumer<Integer, Short> {
    void accept(int t, short u);

    default void accept(Integer t, Short u) {
        this.accept(t == null ? (int) 0 : t, u == null ? (short) 0 : u);
    }
}
