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
public interface DoubleIntBiConsumer extends BiConsumer<Double, Integer> {
    void accept(double t, int u);

    default void accept(Double t, Integer u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (int) 0);
    }
}
