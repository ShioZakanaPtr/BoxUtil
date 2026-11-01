package org.boxutil.util.function.longtype;

import java.util.function.UnaryOperator;

public interface LongUnaryOperator extends UnaryOperator<Long> {
    long apply(long t);

    default Long apply(Long t) {
        return this.apply(t == null ? (long) 0 : t);
    }
}
