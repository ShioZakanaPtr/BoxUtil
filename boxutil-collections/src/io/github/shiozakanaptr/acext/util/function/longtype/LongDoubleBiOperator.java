package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongDoubleBiOperator extends BiFunction<Long, Double, Double> {
    double apply(long t, double u);

    default Double apply(Long t, Double u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (double) 0);
    }
}
