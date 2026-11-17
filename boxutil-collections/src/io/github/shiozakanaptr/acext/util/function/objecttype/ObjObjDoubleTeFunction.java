package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

@FunctionalInterface
public interface ObjObjDoubleTeFunction<T, U, R> extends TeFunction<T, U, Double, R> {
    R apply(T t, U u, double v);

    default R apply(T t, U u, Double v) {
        return this.apply(t, u, v != null ? v : (double) 0);
    }
}
