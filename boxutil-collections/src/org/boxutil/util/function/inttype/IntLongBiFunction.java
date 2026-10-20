package org.boxutil.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntLongBiFunction<R> extends BiFunction<Integer, Long, R> {
    R apply(int t, long u);

    default R apply(Integer t, Long u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (long) 0 : u);
    }
}
