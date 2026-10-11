package org.boxutil.util.function.objectt;

import java.util.function.BiFunction;

public interface ObjIntBiOperator<T> extends BiFunction<T, Integer, Integer> {
    int apply(T t, int u);

    default Integer apply(T t, Integer u) {
        return this.apply(t, u == null ? (int) 0 : u);
    }
}
