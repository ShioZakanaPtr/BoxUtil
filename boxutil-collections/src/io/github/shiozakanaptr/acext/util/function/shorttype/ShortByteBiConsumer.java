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
public interface ShortByteBiConsumer extends BiConsumer<Short, Byte> {
    void accept(short t, byte u);

    default void accept(Short t, Byte u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (byte) 0);
    }
}
