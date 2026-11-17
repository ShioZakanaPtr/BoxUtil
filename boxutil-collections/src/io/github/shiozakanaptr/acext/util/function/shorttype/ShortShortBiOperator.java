package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortShortBiOperator extends BiFunction<Short, Short, Short> {
    short apply(short t, short u);

    default Short apply(Short t, Short u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (short) 0);
    }
}
