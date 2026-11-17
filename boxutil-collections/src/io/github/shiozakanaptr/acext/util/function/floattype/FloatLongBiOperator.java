package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatLongBiOperator extends BiFunction<Float, Long, Long> {
    long apply(float t, long u);

    default Long apply(Float t, Long u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (long) 0);
    }
}
