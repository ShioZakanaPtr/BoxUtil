package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortDoubleBiConsumer extends BiConsumer<Short, Double> {
    void accept(short t, double u);

    default void accept(Short t, Double u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (double) 0);
    }
}
