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
public interface FloatByteBiConsumer extends BiConsumer<Float, Byte> {
    void accept(float t, byte u);

    default void accept(Float t, Byte u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (byte) 0);
    }
}
