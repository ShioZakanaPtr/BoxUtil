package io.github.shiozakanaptr.acext.util.function.chartype;

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
public interface CharLongBiOperator extends BiFunction<Character, Long, Long> {
    long apply(char t, long u);

    default Long apply(Character t, Long u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (long) 0);
    }
}
