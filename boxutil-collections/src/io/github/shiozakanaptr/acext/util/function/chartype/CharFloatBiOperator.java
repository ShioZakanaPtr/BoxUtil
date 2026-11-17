package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharFloatBiOperator extends BiFunction<Character, Float, Float> {
    float apply(char t, float u);

    default Float apply(Character t, Float u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (float) 0);
    }
}
