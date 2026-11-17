package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeFunction;

@FunctionalInterface
public interface ObjObjShortTeFunction<T, U, R> extends TeFunction<T, U, Short, R> {
    R apply(T t, U u, short v);

    default R apply(T t, U u, Short v) {
        return this.apply(t, u, v != null ? v : (short) 0);
    }
}
