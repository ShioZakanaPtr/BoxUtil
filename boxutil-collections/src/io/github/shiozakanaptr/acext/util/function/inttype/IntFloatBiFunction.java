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
public interface IntFloatBiFunction<R> extends BiFunction<Integer, Float, R> {
    R apply(int t, float u);

    default R apply(Integer t, Float u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (float) 0);
    }
}
