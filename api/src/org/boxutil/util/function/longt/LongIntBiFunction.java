package org.boxutil.util.function.longt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongIntBiFunction<R> extends BiFunction<Long, Integer, R> {
    R apply(long t, int u);

    default R apply(Long t, Integer u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (int) 0 : u);
    }
}
