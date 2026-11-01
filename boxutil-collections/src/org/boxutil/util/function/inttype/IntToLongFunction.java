package org.boxutil.util.function.inttype;

import java.util.function.Function;

public interface IntToLongFunction extends Function<Integer, Long> {
    long apply(int t);

    default Long apply(Integer t) {
        return this.apply(t == null ? (int) 0 : t);
    }
}
