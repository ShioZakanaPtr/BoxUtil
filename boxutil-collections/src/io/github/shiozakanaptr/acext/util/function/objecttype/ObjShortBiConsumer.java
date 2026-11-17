package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ObjShortBiConsumer<T> extends BiConsumer<T, Short> {
    void accept(T t, short u);

    default void accept(T t, Short u) {
        this.accept(t, u != null ? u : (short) 0);
    }
}
