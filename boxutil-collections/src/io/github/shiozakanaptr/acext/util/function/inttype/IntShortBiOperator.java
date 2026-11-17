package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntShortBiOperator extends BiFunction<Integer, Short, Short> {
    short apply(int t, short u);

    default Short apply(Integer t, Short u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (short) 0);
    }
}
