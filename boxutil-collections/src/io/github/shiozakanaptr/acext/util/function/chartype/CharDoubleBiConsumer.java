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
public interface CharDoubleBiConsumer extends BiConsumer<Character, Double> {
    void accept(char t, double u);

    default void accept(Character t, Double u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (double) 0);
    }
}
