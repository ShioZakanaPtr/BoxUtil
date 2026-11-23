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
public interface CharLongBiFunction<R> extends BiFunction<Character, Long, R> {
    R apply(char t, long u);

    default R apply(Character t, Long u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (long) 0);
    }
}
