package org.boxutil.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortIntBiFunction<R> extends BiFunction<Short, Integer, R> {
    R apply(short t, int u);

    default R apply(Short t, Integer u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (int) 0 : u);
    }
}
