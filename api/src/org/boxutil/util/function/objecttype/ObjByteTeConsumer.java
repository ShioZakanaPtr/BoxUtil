package org.boxutil.util.function.objecttype;

@FunctionalInterface
public interface ObjByteTeConsumer<T, U> extends TeConsumer<T, U, Byte> {
    void accept(T t, U u, byte v);

    default void accept(T t, U u, Byte v) {
        this.accept(t, u, v == null ? (byte) 0 : v);
    }
}
