package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IntLongBiConsumer extends BiConsumer<Integer, Long> {
    void accept(int t, long u);

    default void accept(Integer t, Long u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (long) 0);
    }
}
