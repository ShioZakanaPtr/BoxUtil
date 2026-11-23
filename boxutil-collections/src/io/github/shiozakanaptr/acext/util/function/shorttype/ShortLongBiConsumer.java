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
public interface ShortLongBiConsumer extends BiConsumer<Short, Long> {
    void accept(short t, long u);

    default void accept(Short t, Long u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (long) 0);
    }
}
