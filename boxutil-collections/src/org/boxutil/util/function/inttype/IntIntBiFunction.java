package org.boxutil.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntIntBiFunction<R> extends BiFunction<Integer, Integer, R> {
    R apply(int t, int u);

    default R apply(Integer t, Integer u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (int) 0 : u);
    }
}
