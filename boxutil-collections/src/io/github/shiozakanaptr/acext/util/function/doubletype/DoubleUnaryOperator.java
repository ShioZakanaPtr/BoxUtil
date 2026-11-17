package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface DoubleUnaryOperator extends UnaryOperator<Double> {
    double apply(double t);

    default Double apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
