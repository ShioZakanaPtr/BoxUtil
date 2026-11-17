package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongLongBiFunction<R> extends BiFunction<Long, Long, R> {
    R apply(long t, long u);

    default R apply(Long t, Long u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (long) 0);
    }
}
