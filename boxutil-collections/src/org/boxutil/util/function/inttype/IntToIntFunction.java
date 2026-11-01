package org.boxutil.util.function.inttype;

import java.util.function.Function;

public interface IntToIntFunction extends Function<Integer, Integer> {
    int apply(int t);

    default Integer apply(Integer t) {
        return this.apply(t == null ? (int) 0 : t);
    }
}
