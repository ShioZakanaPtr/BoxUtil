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
public interface DoubleDoubleBiFunction<R> extends BiFunction<Double, Double, R> {
    R apply(double t, double u);

    default R apply(Double t, Double u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (double) 0);
    }
}
