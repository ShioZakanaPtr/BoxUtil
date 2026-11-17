package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IntDoubleBiConsumer extends BiConsumer<Integer, Double> {
    void accept(int t, double u);

    default void accept(Integer t, Double u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (double) 0);
    }
}
