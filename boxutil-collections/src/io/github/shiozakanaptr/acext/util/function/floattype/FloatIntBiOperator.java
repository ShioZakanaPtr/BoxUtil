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
public interface FloatIntBiOperator extends BiFunction<Float, Integer, Integer> {
    int apply(float t, int u);

    default Integer apply(Float t, Integer u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (int) 0);
    }
}
