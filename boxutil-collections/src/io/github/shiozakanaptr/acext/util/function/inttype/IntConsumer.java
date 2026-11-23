package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Consumer;

/**
 * Type specialization functional interface.
 *
 * @see Consumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface IntConsumer extends Consumer<Integer> {
    void accept(int t);

    default void accept(Integer t) {
        this.accept(t != null ? t : (int) 0);
    }
}
