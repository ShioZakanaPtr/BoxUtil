package io.github.shiozakanaptr.acext.util.function.doubletype;

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
public interface DoubleCharBiOperator extends BiFunction<Double, Character, Character> {
    char apply(double t, char u);

    default Character apply(Double t, Character u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (char) 0);
    }
}
