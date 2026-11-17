package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

@FunctionalInterface
public interface ObjObjLongTeFunction<T, U, R> extends TeFunction<T, U, Long, R> {
    R apply(T t, U u, long v);

    default R apply(T t, U u, Long v) {
        return this.apply(t, u, v != null ? v : (long) 0);
    }
}
