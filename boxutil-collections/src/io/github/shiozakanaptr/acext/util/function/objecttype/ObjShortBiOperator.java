package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjShortBiOperator<T> extends BiFunction<T, Short, Short> {
    short apply(T t, short u);

    default Short apply(T t, Short u) {
        return this.apply(t, u != null ? u : (short) 0);
    }
}
