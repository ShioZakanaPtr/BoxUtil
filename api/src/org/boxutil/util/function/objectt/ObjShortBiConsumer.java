package org.boxutil.util.function.objectt;

import java.util.function.BiConsumer;

public interface ObjShortBiConsumer<T> extends BiConsumer<T, Short> {
    void accept(T t, short u);

    default void accept(T t, Short u) {
        this.accept(t, u == null ? (short) 0 : u);
    }
}
