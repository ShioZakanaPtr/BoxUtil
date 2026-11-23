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
public interface DoubleShortBiConsumer extends BiConsumer<Double, Short> {
    void accept(double t, short u);

    default void accept(Double t, Short u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (short) 0);
    }
}
