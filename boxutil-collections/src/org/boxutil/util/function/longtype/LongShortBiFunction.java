package org.boxutil.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongShortBiFunction<R> extends BiFunction<Long, Short, R> {
    R apply(long t, short u);

    default R apply(Long t, Short u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (short) 0 : u);
    }
}
