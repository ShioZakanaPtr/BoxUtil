package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiConsumer;

/**
 * Type specialization functional interface.
 *
 * @see BiConsumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface DoubleFloatBiConsumer extends BiConsumer<Double, Float> {
    void accept(double t, float u);

    default void accept(Double t, Float u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (float) 0);
    }
}
