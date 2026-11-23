package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongByteBiConsumer extends BiConsumer<Long, Byte> {
    void accept(long t, byte u);

    default void accept(Long t, Byte u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (byte) 0);
    }
}
