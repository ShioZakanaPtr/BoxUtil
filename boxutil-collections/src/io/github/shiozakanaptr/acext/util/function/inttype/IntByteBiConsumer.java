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
public interface IntByteBiConsumer extends BiConsumer<Integer, Byte> {
    void accept(int t, byte u);

    default void accept(Integer t, Byte u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (byte) 0);
    }
}
