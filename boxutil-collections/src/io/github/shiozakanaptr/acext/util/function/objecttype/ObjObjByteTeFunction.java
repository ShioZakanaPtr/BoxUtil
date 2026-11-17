package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.objecttype.TeFunction;

@FunctionalInterface
public interface ObjObjByteTeFunction<T, U, R> extends TeFunction<T, U, Byte, R> {
    R apply(T t, U u, byte v);

    default R apply(T t, U u, Byte v) {
        return this.apply(t, u, v != null ? v : (byte) 0);
    }
}
