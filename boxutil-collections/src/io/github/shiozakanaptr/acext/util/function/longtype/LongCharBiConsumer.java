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
public interface LongCharBiConsumer extends BiConsumer<Long, Character> {
    void accept(long t, char u);

    default void accept(Long t, Character u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (char) 0);
    }
}
