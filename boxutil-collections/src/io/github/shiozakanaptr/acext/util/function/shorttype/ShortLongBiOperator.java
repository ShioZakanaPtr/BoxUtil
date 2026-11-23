package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortLongBiOperator extends BiFunction<Short, Long, Long> {
    long apply(short t, long u);

    default Long apply(Short t, Long u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (long) 0);
    }
}
