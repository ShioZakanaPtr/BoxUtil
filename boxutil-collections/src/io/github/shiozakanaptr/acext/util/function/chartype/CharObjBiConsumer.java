package io.github.shiozakanaptr.acext.util.function.chartype;

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
public interface CharObjBiConsumer<U> extends BiConsumer<Character, U> {
    void accept(char t, U u);

    default void accept(Character t, U u) {
        this.accept(t != null ? t : (char) 0, u);
    }
}
