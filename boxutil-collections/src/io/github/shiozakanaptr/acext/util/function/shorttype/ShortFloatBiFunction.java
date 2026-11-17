package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortFloatBiFunction<R> extends BiFunction<Short, Float, R> {
    R apply(short t, float u);

    default R apply(Short t, Float u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (float) 0);
    }
}
