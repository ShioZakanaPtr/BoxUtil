package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjIntBiOperator<T> extends BiFunction<T, Integer, Integer> {
    int apply(T t, int u);

    default Integer apply(T t, Integer u) {
        return this.apply(t,, u != null ? u : (int) 0);
    }
}
