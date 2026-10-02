package org.boxutil.util.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongLongBiFunction<R> extends BiFunction<Long, Long, R> {
    R apply(long t, long u);

    default R apply(Long t, Long u) {
        return this.apply(t == null ? 0 : t, u == null ? 0 : u);
    }
}
