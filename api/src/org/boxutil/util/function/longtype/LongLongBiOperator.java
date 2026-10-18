package org.boxutil.util.function.longtype;

import java.util.function.BiFunction;

public interface LongLongBiOperator extends BiFunction<Long, Long, Long> {
    long apply(long t, long u);

    default Long apply(Long t, Long u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (long) 0 : u);
    }
}
