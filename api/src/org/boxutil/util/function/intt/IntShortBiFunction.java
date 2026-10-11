package org.boxutil.util.function.intt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntShortBiFunction<R> extends BiFunction<Integer, Short, R> {
    R apply(int t, short u);

    default R apply(Integer t, Short u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (short) 0 : u);
    }
}
