package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface DoubleToIntFunction extends Function<Double, Integer> {
    int apply(double t);

    default Integer apply(Double t) {
        return this.apply(t != null ? t : (double) 0);
    }
}
