package org.boxutil.util.function.objecttype;

import java.util.function.BiFunction;

public interface ObjShortBiOperator<T> extends BiFunction<T, Short, Short> {
    short apply(T t, short u);

    default Short apply(T t, Short u) {
        return this.apply(t, u == null ? (short) 0 : u);
    }
}
