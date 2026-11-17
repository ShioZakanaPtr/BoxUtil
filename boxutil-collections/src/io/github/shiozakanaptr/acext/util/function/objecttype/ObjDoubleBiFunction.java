package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjDoubleBiFunction<T, R> extends BiFunction<T, Double, R> {
    R apply(T t, double u);

    default R apply(T t, Double u) {
        return this.apply(t, u != null ? u : (double) 0);
    }
}
