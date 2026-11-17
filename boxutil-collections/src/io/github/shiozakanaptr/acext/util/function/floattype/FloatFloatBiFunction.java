package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatFloatBiFunction<R> extends BiFunction<Float, Float, R> {
    R apply(float t, float u);

    default R apply(Float t, Float u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (float) 0);
    }
}
