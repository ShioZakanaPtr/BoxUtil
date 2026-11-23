package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatDoubleBiOperator extends BiFunction<Float, Double, Double> {
    double apply(float t, double u);

    default Double apply(Float t, Double u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (double) 0);
    }
}
