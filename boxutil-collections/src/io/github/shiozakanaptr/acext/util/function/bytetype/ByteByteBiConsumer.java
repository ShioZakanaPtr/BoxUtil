package io.github.shiozakanaptr.acext.util.function.bytetype;

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
public interface ByteByteBiConsumer extends BiConsumer<Byte, Byte> {
    void accept(byte t, byte u);

    default void accept(Byte t, Byte u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (byte) 0);
    }
}
