package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface DoubleLongBiFunction<R> extends BiFunction<Double, Long, R> {
    R apply(double t, long u);

    default R apply(Double t, Long u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (long) 0);
    }
}
