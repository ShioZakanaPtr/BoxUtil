package org.boxutil.util.function.inttype;

import java.util.function.Function;

public interface IntToDoubleFunction extends Function<Integer, Double> {
    double apply(int t);

    default Double apply(Integer t) {
        return this.apply(t == null ? (int) 0 : t);
    }
}
