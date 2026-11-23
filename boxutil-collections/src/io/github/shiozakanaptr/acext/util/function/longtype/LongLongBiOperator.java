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
public interface LongLongBiOperator extends BiFunction<Long, Long, Long> {
    long apply(long t, long u);

    default Long apply(Long t, Long u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (long) 0);
    }
}
