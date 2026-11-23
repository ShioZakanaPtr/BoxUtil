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
public interface DoubleShortBiOperator extends BiFunction<Double, Short, Short> {
    short apply(double t, short u);

    default Short apply(Double t, Short u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (short) 0);
    }
}
