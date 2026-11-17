package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjFloatBiOperator<T> extends BiFunction<T, Float, Float> {
    float apply(T t, float u);

    default Float apply(T t, Float u) {
        return this.apply(t,, u != null ? u : (float) 0);
    }
}
