package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatUnaryOperator extends UnaryOperator<Float> {
    float apply(float t);

    default Float apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
