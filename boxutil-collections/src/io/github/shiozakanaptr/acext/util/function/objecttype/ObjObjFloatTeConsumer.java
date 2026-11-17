package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeConsumer;

@FunctionalInterface
public interface ObjObjFloatTeConsumer<T, U> extends TeConsumer<T, U, Float> {
    void accept(T t, U u, float v);

    default void accept(T t, U u, Float v) {
        this.accept(t, u, v != null ? v : (float) 0);
    }
}
