package org.boxutil.util.function.shorttype;

import java.util.function.BiConsumer;

public interface ShortIntBiConsumer extends BiConsumer<Short, Integer> {
    void accept(short t, int u);

    default void accept(Short t, Integer u) {
        this.accept(t == null ? (short) 0 : t, u == null ? (int) 0 : u);
    }
}
