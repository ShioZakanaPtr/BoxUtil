package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Function;

/**
 * Type specialization interface.
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface IntToDoubleFunction extends Function<Integer, Double> {
    double apply(int t);

    default Double apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
