package org.boxutil.util.function.intt;

import java.util.function.BiConsumer;

public interface IntObjBiConsumer<U> extends BiConsumer<Integer, U> {
    void accept(int t, U u);

    default void accept(Integer t, U u) {
        this.accept(t == null ? (int) 0 : t, u);
    }
}
