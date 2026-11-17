package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeFunction;

@FunctionalInterface
public interface ObjObjLongTeOperator<T, U> extends TeFunction<T, U, Long, Long> {
    long apply(T t, U u, long v);

    default Long apply(T t, U u, Long v) {
        return this.apply(t, u, v != null ? v : (long) 0);
    }
}
