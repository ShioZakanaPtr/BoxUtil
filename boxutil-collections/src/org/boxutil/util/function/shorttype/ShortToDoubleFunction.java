package org.boxutil.util.function.shorttype;

import java.util.function.Function;

public interface ShortToDoubleFunction extends Function<Short, Double> {
    double apply(short t);

    default Double apply(Short t) {
        return this.apply(t == null ? (short) 0 : t);
    }
}
