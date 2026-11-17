package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatCharBiFunction<R> extends BiFunction<Float, Character, R> {
    R apply(float t, char u);

    default R apply(Float t, Character u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (char) 0);
    }
}
