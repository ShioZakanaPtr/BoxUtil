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
public interface DoubleFloatBiOperator extends BiFunction<Double, Float, Float> {
    float apply(double t, float u);

    default Float apply(Double t, Float u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (float) 0);
    }
}
