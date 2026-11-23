package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortConsumer extends Consumer<Short> {
    void accept(short t);

    default void accept(Short t) {
        this.accept(t != null ? t : (short) 0);
    }
}
