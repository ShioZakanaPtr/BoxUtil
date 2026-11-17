package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntFloatBiOperator extends BiFunction<Integer, Float, Float> {
    float apply(int t, float u);

    default Float apply(Integer t, Float u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (float) 0);
    }
}
