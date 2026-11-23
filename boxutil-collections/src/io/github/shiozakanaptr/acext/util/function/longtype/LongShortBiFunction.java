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
public interface LongShortBiFunction<R> extends BiFunction<Long, Short, R> {
    R apply(long t, short u);

    default R apply(Long t, Short u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (short) 0);
    }
}
