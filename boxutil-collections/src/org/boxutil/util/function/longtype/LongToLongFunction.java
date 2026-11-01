package org.boxutil.util.function.longtype;

import java.util.function.Function;

public interface LongToLongFunction extends Function<Long, Long> {
    long apply(long t);

    default Long apply(Long t) {
        return this.apply(t == null ? (long) 0 : t);
    }
}
