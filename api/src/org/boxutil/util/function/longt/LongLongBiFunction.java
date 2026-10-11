package org.boxutil.util.function.longt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongLongBiFunction<R> extends BiFunction<Long, Long, R> {
    R apply(long t, long u);

    default R apply(Long t, Long u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (long) 0 : u);
    }
}
