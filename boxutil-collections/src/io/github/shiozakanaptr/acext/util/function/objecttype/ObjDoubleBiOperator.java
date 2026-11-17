package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjDoubleBiOperator<T> extends BiFunction<T, Double, Double> {
    double apply(T t, double u);

    default Double apply(T t, Double u) {
        return this.apply(t,, u != null ? u : (double) 0);
    }
}
