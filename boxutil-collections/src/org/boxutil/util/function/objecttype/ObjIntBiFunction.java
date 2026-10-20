package org.boxutil.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjIntBiFunction<T, R> extends BiFunction<T, Integer, R> {
    R apply(T t, int u);

    default R apply(T t, Integer u) {
        return this.apply(t, u == null ? (int) 0 : u);
    }
}
