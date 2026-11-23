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
public interface LongCharBiOperator extends BiFunction<Long, Character, Character> {
    char apply(long t, char u);

    default Character apply(Long t, Character u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (char) 0);
    }
}
