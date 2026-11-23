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
public interface ShortShortBiFunction<R> extends BiFunction<Short, Short, R> {
    R apply(short t, short u);

    default R apply(Short t, Short u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (short) 0);
    }
}
