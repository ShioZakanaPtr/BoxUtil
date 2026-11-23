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
public interface FloatIntBiConsumer extends BiConsumer<Float, Integer> {
    void accept(float t, int u);

    default void accept(Float t, Integer u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (int) 0);
    }
}
