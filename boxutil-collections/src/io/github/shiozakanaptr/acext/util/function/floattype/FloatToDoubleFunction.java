package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface FloatToDoubleFunction extends Function<Float, Double> {
    double apply(float t);

    default Double apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
