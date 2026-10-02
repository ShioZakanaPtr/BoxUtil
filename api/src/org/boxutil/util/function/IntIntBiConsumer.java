package org.boxutil.util.function;

import java.util.function.BiConsumer;

public interface IntIntBiConsumer extends BiConsumer<Integer, Integer> {
    void accept(int t, int u);

    default void accept(Integer t, Integer u) {
        this.accept(t == null ? 0 : t, u == null ? 0 : u);
    }
}
