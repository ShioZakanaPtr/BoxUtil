package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatShortBiFunction<R> extends BiFunction<Float, Short, R> {
    R apply(float t, short u);

    default R apply(Float t, Short u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (short) 0);
    }
}
