package org.boxutil.util.function;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntXBiFunction<U, R> extends BiFunction<Integer, U, R> {
    R apply(int t, U u);

    default R apply(Integer t, U u) {
        return this.apply(t == null ? 0 : t, u);
    }
}
