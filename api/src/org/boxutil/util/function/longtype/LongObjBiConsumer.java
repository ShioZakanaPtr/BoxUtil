package org.boxutil.util.function.longtype;

import java.util.function.BiConsumer;

public interface LongObjBiConsumer<U> extends BiConsumer<Long, U> {
    void accept(long t, U u);

    default void accept(Long t, U u) {
        this.accept(t == null ? (long) 0 : t, u);
    }
}
