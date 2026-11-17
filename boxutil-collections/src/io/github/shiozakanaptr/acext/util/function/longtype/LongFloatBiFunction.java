package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongFloatBiFunction<R> extends BiFunction<Long, Float, R> {
    R apply(long t, float u);

    default R apply(Long t, Float u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (float) 0);
    }
}
