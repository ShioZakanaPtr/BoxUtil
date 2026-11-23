package io.github.shiozakanaptr.acext.util.function.chartype;

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
public interface CharConsumer extends Consumer<Character> {
    void accept(char t);

    default void accept(Character t) {
        this.accept(t != null ? t : (char) 0);
    }
}
