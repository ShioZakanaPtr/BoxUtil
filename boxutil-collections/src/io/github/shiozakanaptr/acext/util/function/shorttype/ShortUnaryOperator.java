package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortUnaryOperator extends UnaryOperator<Short> {
    short apply(short t);

    default Short apply(Short t) {
        return this.apply(t != null ? t : (short) 0);
    }
}
