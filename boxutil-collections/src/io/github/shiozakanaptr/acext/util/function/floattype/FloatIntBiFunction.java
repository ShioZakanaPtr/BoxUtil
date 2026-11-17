package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatIntBiFunction<R> extends BiFunction<Float, Integer, R> {
    R apply(float t, int u);

    default R apply(Float t, Integer u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (int) 0);
    }
}
