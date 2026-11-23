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
public interface IntLongBiOperator extends BiFunction<Integer, Long, Long> {
    long apply(int t, long u);

    default Long apply(Integer t, Long u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (long) 0);
    }
}
