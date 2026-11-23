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
public interface CharDoubleBiOperator extends BiFunction<Character, Double, Double> {
    double apply(char t, double u);

    default Double apply(Character t, Double u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (double) 0);
    }
}
