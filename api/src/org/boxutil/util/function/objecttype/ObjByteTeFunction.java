package org.boxutil.util.function.objecttype;

@FunctionalInterface
public interface ObjByteTeFunction<T, U, R> extends TeFunction<T, U, Byte, R> {
    R apply(T t, U u, byte v);

    default R apply(T t, U u, Byte v) {
        return this.apply(t, u, v == null ? (byte) 0 : v);
    }
}
