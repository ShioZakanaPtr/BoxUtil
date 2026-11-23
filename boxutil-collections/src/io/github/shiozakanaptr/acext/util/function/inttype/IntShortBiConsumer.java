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
public interface IntShortBiConsumer extends BiConsumer<Integer, Short> {
    void accept(int t, short u);

    default void accept(Integer t, Short u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (short) 0);
    }
}
