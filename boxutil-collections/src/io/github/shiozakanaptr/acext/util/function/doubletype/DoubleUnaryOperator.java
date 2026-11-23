package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.UnaryOperator;

/**
 * Type specialization functional interface.
 *
 * @see UnaryOperator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface DoubleUnaryOperator extends UnaryOperator<Double> {
    double apply(double t);

    default Double apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
