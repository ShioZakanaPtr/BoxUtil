package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LongDoubleBiConsumer extends BiConsumer<Long, Double> {
    void accept(long t, double u);

    default void accept(Long t, Double u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (double) 0);
    }
}
