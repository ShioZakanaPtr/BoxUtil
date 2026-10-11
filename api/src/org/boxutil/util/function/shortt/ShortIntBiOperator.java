package org.boxutil.util.function.shortt;

import java.util.function.BiFunction;

public interface ShortIntBiOperator extends BiFunction<Short, Integer, Integer> {
    int apply(short t, int u);

    default Integer apply(Short t, Integer u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (int) 0 : u);
    }
}
