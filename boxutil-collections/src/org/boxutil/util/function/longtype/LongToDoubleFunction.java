package org.boxutil.util.function.longtype;

import java.util.function.Function;

public interface LongToDoubleFunction extends Function<Long, Double> {
    double apply(long t);

    default Double apply(Long t) {
        return this.apply(t == null ? (long) 0 : t);
    }
}
