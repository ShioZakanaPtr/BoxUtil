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
public interface ShortIntBiFunction<R> extends BiFunction<Short, Integer, R> {
    R apply(short t, int u);

    default R apply(Short t, Integer u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (int) 0);
    }
}
