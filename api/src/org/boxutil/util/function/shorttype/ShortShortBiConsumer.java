package org.boxutil.util.function.shorttype;

import java.util.function.BiConsumer;

public interface ShortShortBiConsumer extends BiConsumer<Short, Short> {
    void accept(short t, short u);

    default void accept(Short t, Short u) {
        this.accept(t == null ? (short) 0 : t, u == null ? (short) 0 : u);
    }
}
