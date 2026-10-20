package org.boxutil.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongObjBiFunction<U, R> extends BiFunction<Long, U, R> {
    R apply(long t, U u);

    default R apply(Long t, U u) {
        return this.apply(t == null ? (long) 0 : t, u);
    }
}
