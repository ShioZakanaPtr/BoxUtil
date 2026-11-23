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
public interface IntIntBiFunction<R> extends BiFunction<Integer, Integer, R> {
    R apply(int t, int u);

    default R apply(Integer t, Integer u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (int) 0);
    }
}
