package org.boxutil.util.function.shortt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortLongBiFunction<R> extends BiFunction<Short, Long, R> {
    R apply(short t, long u);

    default R apply(Short t, Long u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (long) 0 : u);
    }
}
