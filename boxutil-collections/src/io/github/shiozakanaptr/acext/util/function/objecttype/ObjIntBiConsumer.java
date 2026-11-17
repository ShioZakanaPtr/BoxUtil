package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ObjIntBiConsumer<T> extends BiConsumer<T, Integer> {
    void accept(T t, int u);

    default void accept(T t, Integer u) {
        this.accept(t, u != null ? u : (int) 0);
    }
}
