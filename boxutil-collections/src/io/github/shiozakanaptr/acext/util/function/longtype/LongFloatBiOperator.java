package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongFloatBiOperator extends BiFunction<Long, Float, Float> {
    float apply(long t, float u);

    default Float apply(Long t, Float u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (float) 0);
    }
}
