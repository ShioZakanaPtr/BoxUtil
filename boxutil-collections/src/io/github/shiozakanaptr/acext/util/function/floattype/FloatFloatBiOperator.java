package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatFloatBiOperator extends BiFunction<Float, Float, Float> {
    float apply(float t, float u);

    default Float apply(Float t, Float u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (float) 0);
    }
}
