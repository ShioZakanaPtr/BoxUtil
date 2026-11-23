package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongConsumer extends Consumer<Long> {
    void accept(long t);

    default void accept(Long t) {
        this.accept(t != null ? t : (long) 0);
    }
}
