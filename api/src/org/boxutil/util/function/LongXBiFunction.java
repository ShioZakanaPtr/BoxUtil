package org.boxutil.util.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongXBiFunction<U, R> extends BiFunction<Long, U, R> {
    R apply(long t, U u);

    default R apply(Long t, U u) {
        return this.apply(t == null ? 0 : t, u);
    }
}
