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
public interface CharFloatBiFunction<R> extends BiFunction<Character, Float, R> {
    R apply(char t, float u);

    default R apply(Character t, Float u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (float) 0);
    }
}
