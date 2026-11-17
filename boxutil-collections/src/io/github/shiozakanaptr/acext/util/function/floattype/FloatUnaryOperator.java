package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface FloatUnaryOperator extends UnaryOperator<Float> {
    float apply(float t);

    default Float apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
