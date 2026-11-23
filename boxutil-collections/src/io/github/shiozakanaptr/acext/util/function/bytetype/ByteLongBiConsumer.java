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
public interface ByteLongBiConsumer extends BiConsumer<Byte, Long> {
    void accept(byte t, long u);

    default void accept(Byte t, Long u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (long) 0);
    }
}
