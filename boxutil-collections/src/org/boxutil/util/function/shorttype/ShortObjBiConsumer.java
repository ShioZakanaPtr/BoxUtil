package org.boxutil.util.function.shorttype;

import java.util.function.BiConsumer;

public interface ShortObjBiConsumer<U> extends BiConsumer<Short, U> {
    void accept(short t, U u);

    default void accept(Short t, U u) {
        this.accept(t == null ? (short) 0 : t, u);
    }
}
