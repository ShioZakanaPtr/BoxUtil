package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface DoubleToDoubleFunction extends Function<Double, Double> {
    double apply(double t);

    default Double apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
