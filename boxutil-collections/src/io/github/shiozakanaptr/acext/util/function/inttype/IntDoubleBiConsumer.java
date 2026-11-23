package io.github.shiozakanaptr.acext.util.function.inttype;

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
public interface IntDoubleBiConsumer extends BiConsumer<Integer, Double> {
    void accept(int t, double u);

    default void accept(Integer t, Double u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (double) 0);
    }
}
