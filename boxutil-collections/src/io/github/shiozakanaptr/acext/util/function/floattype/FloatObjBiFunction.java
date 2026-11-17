package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatObjBiFunction<U, R> extends BiFunction<Float, U, R> {
    R apply(float t, U u);

    default R apply(Float t, U u) {
        return this.apply(t != null ? t : (float) 0, u);
    }
}
