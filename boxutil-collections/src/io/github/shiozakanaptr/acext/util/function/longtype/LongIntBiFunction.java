package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongIntBiFunction<R> extends BiFunction<Long, Integer, R> {
    R apply(long t, int u);

    default R apply(Long t, Integer u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (int) 0);
    }
}
