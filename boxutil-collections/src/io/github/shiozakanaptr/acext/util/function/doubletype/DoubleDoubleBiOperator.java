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
public interface DoubleDoubleBiOperator extends BiFunction<Double, Double, Double> {
    double apply(double t, double u);

    default Double apply(Double t, Double u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (double) 0);
    }
}
