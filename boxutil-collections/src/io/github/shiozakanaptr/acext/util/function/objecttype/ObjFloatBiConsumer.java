package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ObjFloatBiConsumer<T> extends BiConsumer<T, Float> {
    void accept(T t, float u);

    default void accept(T t, Float u) {
        this.accept(t, u != null ? u : (float) 0);
    }
}
