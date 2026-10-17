package org.boxutil.util.function.intt;

import java.util.function.BiFunction;

public interface IntIntBiOperator extends BiFunction<Integer, Integer, Integer> {
    int apply(int t, int u);

    default Integer apply(Integer t, Integer u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (int) 0 : u);
    }
}
