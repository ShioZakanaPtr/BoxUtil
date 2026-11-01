package org.boxutil.util.function.longtype;

import java.util.function.Function;

public interface LongToIntFunction extends Function<Long, Integer> {
    int apply(long t);

    default Integer apply(Long t) {
        return this.apply(t == null ? (long) 0 : t);
    }
}
