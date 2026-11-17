package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

@FunctionalInterface
public interface ObjObjShortTeOperator<T, U> extends TeFunction<T, U, Short, Short> {
    short apply(T t, U u, short v);

    default Short apply(T t, U u, Short v) {
        return this.apply(t, u, v != null ? v : (short) 0);
    }
}
