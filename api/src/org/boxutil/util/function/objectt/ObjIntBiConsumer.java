package org.boxutil.util.function.objectt;

import java.util.function.BiConsumer;

public interface ObjIntBiConsumer<T> extends BiConsumer<T, Integer> {
    void accept(T t, int u);

    default void accept(T t, Integer u) {
        this.accept(t, u == null ? (int) 0 : u);
    }
}
