package org.boxutil.util.function.shortt;

import java.util.function.Function;

public interface ShortFunction<R> extends Function<Short, R> {
    R apply(short t);

    default R apply(Short t) {
        return this.apply(t == null ? (short) 0 : t);
    }
}
