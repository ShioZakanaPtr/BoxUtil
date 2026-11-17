package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface FloatCharBiOperator extends BiFunction<Float, Character, Character> {
    char apply(float t, char u);

    default Character apply(Float t, Character u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (char) 0);
    }
}
