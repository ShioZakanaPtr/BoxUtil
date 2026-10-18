package org.boxutil.util.function.objecttype;

import java.util.function.BiConsumer;

public interface ObjLongBiConsumer<T> extends BiConsumer<T, Long> {
    void accept(T t, long u);

    default void accept(T t, Long u) {
        this.accept(t, u == null ? (long) 0 : u);
    }
}
