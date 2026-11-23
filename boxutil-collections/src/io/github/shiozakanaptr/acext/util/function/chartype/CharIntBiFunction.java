package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

/**
 * Type specialization functional interface.
 *
 * @see BiFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface CharIntBiFunction<R> extends BiFunction<Character, Integer, R> {
    R apply(char t, int u);

    default R apply(Character t, Integer u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (int) 0);
    }
}
