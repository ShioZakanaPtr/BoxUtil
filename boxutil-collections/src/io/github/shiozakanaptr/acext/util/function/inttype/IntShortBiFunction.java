package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntShortBiFunction<R> extends BiFunction<Integer, Short, R> {
    R apply(int t, short u);

    default R apply(Integer t, Short u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (short) 0);
    }
}
