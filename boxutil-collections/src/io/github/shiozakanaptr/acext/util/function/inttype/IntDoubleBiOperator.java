package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntDoubleBiOperator extends BiFunction<Integer, Double, Double> {
    double apply(int t, double u);

    default Double apply(Integer t, Double u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (double) 0);
    }
}
