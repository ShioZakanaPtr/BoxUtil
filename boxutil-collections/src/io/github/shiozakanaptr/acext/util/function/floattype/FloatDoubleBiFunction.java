package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatDoubleBiFunction<R> extends BiFunction<Float, Double, R> {
    R apply(float t, double u);

    default R apply(Float t, Double u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (double) 0);
    }
}
