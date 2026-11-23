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
public interface DoubleLongBiFunction<R> extends BiFunction<Double, Long, R> {
    R apply(double t, long u);

    default R apply(Double t, Long u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (long) 0);
    }
}
