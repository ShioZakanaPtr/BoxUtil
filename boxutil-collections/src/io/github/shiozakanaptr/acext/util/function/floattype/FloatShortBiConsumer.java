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
public interface FloatShortBiConsumer extends BiConsumer<Float, Short> {
    void accept(float t, short u);

    default void accept(Float t, Short u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (short) 0);
    }
}
