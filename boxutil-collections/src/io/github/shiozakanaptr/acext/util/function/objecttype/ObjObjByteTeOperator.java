package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeFunction;

@FunctionalInterface
public interface ObjObjByteTeOperator<T, U> extends TeFunction<T, U, Byte, Byte> {
    byte apply(T t, U u, byte v);

    default Byte apply(T t, U u, Byte v) {
        return this.apply(t, u, v != null ? v : (byte) 0);
    }
}
