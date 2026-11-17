package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

@FunctionalInterface
public interface ObjObjDoubleTeOperator<T, U> extends TeFunction<T, U, Double, Double> {
    double apply(T t, U u, double v);

    default Double apply(T t, U u, Double v) {
        return this.apply(t, u, v != null ? v : (double) 0);
    }
}
