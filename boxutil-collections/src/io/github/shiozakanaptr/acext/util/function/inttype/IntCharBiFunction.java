package io.github.shiozakanaptr.acext.util.function.inttype;

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
public interface IntCharBiFunction<R> extends BiFunction<Integer, Character, R> {
    R apply(int t, char u);

    default R apply(Integer t, Character u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (char) 0);
    }
}
