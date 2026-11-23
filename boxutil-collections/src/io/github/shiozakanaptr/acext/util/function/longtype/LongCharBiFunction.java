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
public interface LongCharBiFunction<R> extends BiFunction<Long, Character, R> {
    R apply(long t, char u);

    default R apply(Long t, Character u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (char) 0);
    }
}
