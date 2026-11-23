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
public interface CharFloatBiConsumer extends BiConsumer<Character, Float> {
    void accept(char t, float u);

    default void accept(Character t, Float u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (float) 0);
    }
}
