package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.UnaryOperator;

/**
 * Type specialization functional interface.
 *
 * @see UnaryOperator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface IntUnaryOperator extends UnaryOperator<Integer> {
    int apply(int t);

    default Integer apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
