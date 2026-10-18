package org.boxutil.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjShortBiFunction<T, R> extends BiFunction<T, Short, R> {
    R apply(T t, short u);

    default R apply(T t, Short u) {
        return this.apply(t, u == null ? (short) 0 : u);
    }
}
