package org.boxutil.util.function.shortt;

import java.util.function.Function;

public interface ShortToLongFunction extends Function<Short, Long> {
    long apply(short t);

    default Long apply(Short t) {
        return this.apply(t == null ? (short) 0 : t);
    }
}
