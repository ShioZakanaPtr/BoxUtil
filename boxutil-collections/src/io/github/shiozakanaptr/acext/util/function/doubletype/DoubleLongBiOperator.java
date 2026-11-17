package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface DoubleLongBiOperator extends BiFunction<Double, Long, Long> {
    long apply(double t, long u);

    default Long apply(Double t, Long u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (long) 0);
    }
}
