package org.boxutil.util.function;

import java.util.function.BiConsumer;

public interface IntXBiConsumer<U> extends BiConsumer<Integer, U> {
    void accept(int t, U u);

    default void accept(Integer t, U u) {
        this.accept(t == null ? 0 : t, u);
    }
}
