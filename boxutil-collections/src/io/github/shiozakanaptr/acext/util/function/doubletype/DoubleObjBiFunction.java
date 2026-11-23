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
public interface DoubleObjBiFunction<U, R> extends BiFunction<Double, U, R> {
    R apply(double t, U u);

    default R apply(Double t, U u) {
        return this.apply(t != null ? t : (double) 0, u);
    }
}
