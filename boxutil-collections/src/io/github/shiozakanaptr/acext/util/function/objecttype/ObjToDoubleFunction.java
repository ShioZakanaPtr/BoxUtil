package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface ObjToDoubleFunction<T> extends ToDoubleFunction<T> {
    double apply(T t);

    default double applyAsDouble(T t) {
        return this.apply(t);
    }
}
