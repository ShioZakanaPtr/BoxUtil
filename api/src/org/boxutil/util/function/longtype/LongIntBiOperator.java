package org.boxutil.util.function.longtype;

import java.util.function.BiFunction;

public interface LongIntBiOperator extends BiFunction<Long, Integer, Integer> {
    int apply(long t, int u);

    default Integer apply(Long t, Integer u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (int) 0 : u);
    }
}
