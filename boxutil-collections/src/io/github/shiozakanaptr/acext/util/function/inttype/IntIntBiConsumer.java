package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IntIntBiConsumer extends BiConsumer<Integer, Integer> {
    void accept(int t, int u);

    default void accept(Integer t, Integer u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (int) 0);
    }
}
