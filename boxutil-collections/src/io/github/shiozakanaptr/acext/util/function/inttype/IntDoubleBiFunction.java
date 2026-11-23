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
public interface IntDoubleBiFunction<R> extends BiFunction<Integer, Double, R> {
    R apply(int t, double u);

    default R apply(Integer t, Double u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (double) 0);
    }
}
