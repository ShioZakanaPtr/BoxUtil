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
public interface DoubleIntBiFunction<R> extends BiFunction<Double, Integer, R> {
    R apply(double t, int u);

    default R apply(Double t, Integer u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (int) 0);
    }
}
