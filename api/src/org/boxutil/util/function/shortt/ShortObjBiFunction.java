package org.boxutil.util.function.shortt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortObjBiFunction<U, R> extends BiFunction<Short, U, R> {
    R apply(short t, U u);

    default R apply(Short t, U u) {
        return this.apply(t == null ? (short) 0 : t, u);
    }
}
