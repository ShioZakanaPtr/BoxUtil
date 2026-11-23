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
public interface CharIntBiConsumer extends BiConsumer<Character, Integer> {
    void accept(char t, int u);

    default void accept(Character t, Integer u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (int) 0);
    }
}
