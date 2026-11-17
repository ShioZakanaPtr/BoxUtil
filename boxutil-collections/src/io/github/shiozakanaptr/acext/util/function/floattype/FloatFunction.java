package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Function;

@FunctionalInterface
public interface FloatFunction<R> extends Function<Float, R> {
    R apply(float t);

    default R apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
