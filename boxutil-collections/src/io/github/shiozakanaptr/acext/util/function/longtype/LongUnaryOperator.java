package io.github.shiozakanaptr.acext.util.function.longtype;

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
public interface LongUnaryOperator extends UnaryOperator<Long> {
    long apply(long t);

    default Long apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
