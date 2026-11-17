package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntFloatBiFunction<R> extends BiFunction<Integer, Float, R> {
    R apply(int t, float u);

    default R apply(Integer t, Float u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (float) 0);
    }
}
