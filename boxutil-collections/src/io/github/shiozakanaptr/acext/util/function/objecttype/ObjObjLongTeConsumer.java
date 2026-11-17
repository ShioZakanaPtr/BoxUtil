package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeConsumer;

@FunctionalInterface
public interface ObjObjLongTeConsumer<T, U> extends TeConsumer<T, U, Long> {
    void accept(T t, U u, long v);

    default void accept(T t, U u, Long v) {
        this.accept(t, u, v != null ? v : (long) 0);
    }
}
