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
public interface ShortLongBiFunction<R> extends BiFunction<Short, Long, R> {
    R apply(short t, long u);

    default R apply(Short t, Long u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (long) 0);
    }
}
