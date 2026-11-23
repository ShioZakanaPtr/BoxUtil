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
public interface FloatFloatBiConsumer extends BiConsumer<Float, Float> {
    void accept(float t, float u);

    default void accept(Float t, Float u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (float) 0);
    }
}
