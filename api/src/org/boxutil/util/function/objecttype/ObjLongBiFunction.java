package org.boxutil.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjLongBiFunction<T, R> extends BiFunction<T, Long, R> {
    R apply(T t, long u);

    default R apply(T t, Long u) {
        return this.apply(t, u == null ? (long) 0 : u);
    }
}
