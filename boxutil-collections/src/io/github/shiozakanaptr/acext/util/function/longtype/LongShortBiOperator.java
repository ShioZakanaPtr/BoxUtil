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
public interface LongShortBiOperator extends BiFunction<Long, Short, Short> {
    short apply(long t, short u);

    default Short apply(Long t, Short u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (short) 0);
    }
}
