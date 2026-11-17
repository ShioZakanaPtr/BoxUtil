package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortFloatBiOperator extends BiFunction<Short, Float, Float> {
    float apply(short t, float u);

    default Float apply(Short t, Float u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (float) 0);
    }
}
