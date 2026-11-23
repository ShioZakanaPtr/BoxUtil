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
public interface IntFloatBiConsumer extends BiConsumer<Integer, Float> {
    void accept(int t, float u);

    default void accept(Integer t, Float u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (float) 0);
    }
}
