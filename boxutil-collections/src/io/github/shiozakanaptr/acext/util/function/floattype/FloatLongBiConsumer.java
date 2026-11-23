package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatLongBiConsumer extends BiConsumer<Float, Long> {
    void accept(float t, long u);

    default void accept(Float t, Long u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (long) 0);
    }
}
