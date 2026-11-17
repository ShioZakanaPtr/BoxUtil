package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeConsumer;

@FunctionalInterface
public interface ObjObjDoubleTeConsumer<T, U> extends TeConsumer<T, U, Double> {
    void accept(T t, U u, double v);

    default void accept(T t, U u, Double v) {
        this.accept(t, u, v != null ? v : (double) 0);
    }
}
