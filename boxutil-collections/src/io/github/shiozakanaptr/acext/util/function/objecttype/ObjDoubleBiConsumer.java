package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ObjDoubleBiConsumer<T> extends BiConsumer<T, Double> {
    void accept(T t, double u);

    default void accept(T t, Double u) {
        this.accept(t, u != null ? u : (double) 0);
    }
}
