package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface IntUnaryOperator extends UnaryOperator<Integer> {
    int apply(int t);

    default Integer apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
