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
public interface DoubleDoubleBiConsumer extends BiConsumer<Double, Double> {
    void accept(double t, double u);

    default void accept(Double t, Double u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (double) 0);
    }
}
