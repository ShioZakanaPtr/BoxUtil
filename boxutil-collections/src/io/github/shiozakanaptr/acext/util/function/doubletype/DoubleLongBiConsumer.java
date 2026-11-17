package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface DoubleLongBiConsumer extends BiConsumer<Double, Long> {
    void accept(double t, long u);

    default void accept(Double t, Long u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (long) 0);
    }
}
