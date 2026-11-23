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
public interface IntObjBiConsumer<U> extends BiConsumer<Integer, U> {
    void accept(int t, U u);

    default void accept(Integer t, U u) {
        this.accept(t != null ? t : (int) 0, u);
    }
}
