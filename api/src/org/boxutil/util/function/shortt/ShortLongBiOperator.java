package org.boxutil.util.function.shortt;

import java.util.function.BiFunction;

public interface ShortLongBiOperator extends BiFunction<Short, Long, Long> {
    long apply(short t, long u);

    default Long apply(Short t, Long u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (long) 0 : u);
    }
}
