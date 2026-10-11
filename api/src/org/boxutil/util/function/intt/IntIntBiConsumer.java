package org.boxutil.util.function.intt;

import java.util.function.BiConsumer;

public interface IntIntBiConsumer extends BiConsumer<Integer, Integer> {
    void accept(int t, int u);

    default void accept(Integer t, Integer u) {
        this.accept(t == null ? (int) 0 : t, u == null ? (int) 0 : u);
    }
}
