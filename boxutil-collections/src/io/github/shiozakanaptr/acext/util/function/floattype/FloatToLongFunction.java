package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Function;

@FunctionalInterface
public interface FloatToLongFunction extends Function<Float, Long> {
    long apply(float t);

    default Long apply(Float t) {
        return this.apply(t != null ? t : (float) 0);
    }
}
