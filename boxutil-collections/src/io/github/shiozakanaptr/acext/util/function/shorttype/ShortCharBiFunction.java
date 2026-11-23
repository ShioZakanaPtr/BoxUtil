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
public interface ShortCharBiFunction<R> extends BiFunction<Short, Character, R> {
    R apply(short t, char u);

    default R apply(Short t, Character u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (char) 0);
    }
}
