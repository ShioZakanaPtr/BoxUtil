package org.boxutil.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortShortBiFunction<R> extends BiFunction<Short, Short, R> {
    R apply(short t, short u);

    default R apply(Short t, Short u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (short) 0 : u);
    }
}
