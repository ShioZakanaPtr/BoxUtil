package io.github.shiozakanaptr.acext.util.function.objecttype;

import io.github.shiozakanaptr.acext.util.function.TeConsumer;

/**
 * Type specialization functional interface.
 *
 * @see TeConsumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjObjByteTeConsumer<T, U> extends TeConsumer<T, U, Byte> {
    void accept(T t, U u, byte v);

    default void accept(T t, U u, Byte v) {
        this.accept(t, u, v != null ? v : (byte) 0);
    }
}
