package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeConsumer;

@FunctionalInterface
public interface ObjObjCharTeConsumer<T, U> extends TeConsumer<T, U, Character> {
    void accept(T t, U u, char v);

    default void accept(T t, U u, Character v) {
        this.accept(t, u, v != null ? v : (char) 0);
    }
}
