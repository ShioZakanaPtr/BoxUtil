package org.boxutil.util.function.shorttype;

import java.util.function.Function;

public interface ShortToIntFunction extends Function<Short, Integer> {
    int apply(short t);

    default Integer apply(Short t) {
        return this.apply(t == null ? (short) 0 : t);
    }
}
