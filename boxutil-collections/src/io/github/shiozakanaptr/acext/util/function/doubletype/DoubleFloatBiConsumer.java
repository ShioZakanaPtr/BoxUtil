package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface DoubleFloatBiConsumer extends BiConsumer<Double, Float> {
    void accept(double t, float u);

    default void accept(Double t, Float u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (float) 0);
    }
}
