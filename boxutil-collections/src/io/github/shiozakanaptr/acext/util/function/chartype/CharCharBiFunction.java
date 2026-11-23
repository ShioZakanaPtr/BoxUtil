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
public interface CharCharBiFunction<R> extends BiFunction<Character, Character, R> {
    R apply(char t, char u);

    default R apply(Character t, Character u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (char) 0);
    }
}
