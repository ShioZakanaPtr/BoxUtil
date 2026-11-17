package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.ToLongFunction;

@FunctionalInterface
public interface ObjToLongFunction<T> extends ToLongFunction<T> {
    long apply(T t);

    default long applyAsLong(T t) {
        return this.apply(t);
    }
}
