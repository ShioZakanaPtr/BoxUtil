package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

@FunctionalInterface
public interface ObjLongTeOperator<T, U> extends TeFunction<T, U, Long, Long> {
    long apply(T t, U u, long v);

    default Long apply(T t, U u, Long v) {
        return this.apply(t, u, v == null ? (long) 0 : v);
    }
}
