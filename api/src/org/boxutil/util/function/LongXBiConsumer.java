package org.boxutil.util.function;

import java.util.function.BiConsumer;

public interface LongXBiConsumer<U> extends BiConsumer<Long, U> {
    void accept(long t, U u);

    default void accept(Long t, U u) {
        this.accept(t == null ? 0 : t, u);
    }
}
