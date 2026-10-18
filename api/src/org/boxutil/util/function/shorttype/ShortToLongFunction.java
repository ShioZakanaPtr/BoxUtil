package org.boxutil.util.function.shorttype;

import java.util.function.Function;

public interface ShortToLongFunction extends Function<Short, Long> {
    long apply(short t);

    default Long apply(Short t) {
        return this.apply(t == null ? (short) 0 : t);
    }
}
