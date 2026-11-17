package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Function;

@FunctionalInterface
public interface IntToDoubleFunction extends Function<Integer, Double> {
    double apply(int t);

    default Double apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
