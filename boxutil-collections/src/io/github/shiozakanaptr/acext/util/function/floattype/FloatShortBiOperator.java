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
public interface FloatShortBiOperator extends BiFunction<Float, Short, Short> {
    short apply(float t, short u);

    default Short apply(Float t, Short u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (short) 0);
    }
}
