package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongDoubleBiConsumer extends BiConsumer<Long, Double> {
    void accept(long t, double u);

    default void accept(Long t, Double u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (double) 0);
    }
}
