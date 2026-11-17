package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongDoubleBiFunction<R> extends BiFunction<Long, Double, R> {
    R apply(long t, double u);

    default R apply(Long t, Double u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (double) 0);
    }
}
