package org.boxutil.util.function.objecttype;

@FunctionalInterface
public interface ObjByteTeOperator<T, U> extends TeFunction<T, U, Byte, Byte> {
    byte apply(T t, U u, byte v);

    default Byte apply(T t, U u, Byte v) {
        return this.apply(t, u, v == null ? (byte) 0 : v);
    }
}
