package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongIntBiOperator extends BiFunction<Long, Integer, Integer> {
    int apply(long t, int u);

    default Integer apply(Long t, Integer u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (int) 0);
    }
}
