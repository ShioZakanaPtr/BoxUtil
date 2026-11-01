package org.boxutil.util.function.inttype;

import java.util.function.UnaryOperator;

public interface IntUnaryOperator extends UnaryOperator<Integer> {
    int apply(int t);

    default Integer apply(Integer t) {
        return this.apply(t == null ? (int) 0 : t);
    }
}
