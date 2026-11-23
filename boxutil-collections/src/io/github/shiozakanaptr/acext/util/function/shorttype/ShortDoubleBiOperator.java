package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortDoubleBiOperator extends BiFunction<Short, Double, Double> {
    double apply(short t, double u);

    default Double apply(Short t, Double u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (double) 0);
    }
}
