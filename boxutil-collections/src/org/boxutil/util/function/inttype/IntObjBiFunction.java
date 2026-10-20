package org.boxutil.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntObjBiFunction<U, R> extends BiFunction<Integer, U, R> {
    R apply(int t, U u);

    default R apply(Integer t, U u) {
        return this.apply(t == null ? (int) 0 : t, u);
    }
}
