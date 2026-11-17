package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjFloatBiFunction<T, R> extends BiFunction<T, Float, R> {
    R apply(T t, float u);

    default R apply(T t, Float u) {
        return this.apply(t, u != null ? u : (float) 0);
    }
}
