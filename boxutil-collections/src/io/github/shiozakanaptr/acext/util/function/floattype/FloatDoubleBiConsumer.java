package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface FloatDoubleBiConsumer extends BiConsumer<Float, Double> {
    void accept(float t, double u);

    default void accept(Float t, Double u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (double) 0);
    }
}
