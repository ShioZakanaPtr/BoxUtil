package org.boxutil.util.function.longt;

import java.util.function.BiFunction;

public interface LongShortBiOperator extends BiFunction<Long, Short, Short> {
    short apply(long t, short u);

    default Short apply(Long t, Short u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (short) 0 : u);
    }
}
