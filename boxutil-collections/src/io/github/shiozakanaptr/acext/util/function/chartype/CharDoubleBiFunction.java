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
public interface CharDoubleBiFunction<R> extends BiFunction<Character, Double, R> {
    R apply(char t, double u);

    default R apply(Character t, Double u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (double) 0);
    }
}
