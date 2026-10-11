package org.boxutil.util.function.intt;

import java.util.function.BiFunction;

public interface IntLongBiOperator extends BiFunction<Integer, Long, Long> {
    long apply(int t, long u);

    default Long apply(Integer t, Long u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (long) 0 : u);
    }
}
