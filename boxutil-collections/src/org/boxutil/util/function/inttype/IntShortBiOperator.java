package org.boxutil.util.function.inttype;

import java.util.function.BiFunction;

public interface IntShortBiOperator extends BiFunction<Integer, Short, Short> {
    short apply(int t, short u);

    default Short apply(Integer t, Short u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (short) 0 : u);
    }
}
