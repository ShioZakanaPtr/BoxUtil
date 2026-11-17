package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatLongBiFunction<R> extends BiFunction<Float, Long, R> {
    R apply(float t, long u);

    default R apply(Float t, Long u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (long) 0);
    }
}
