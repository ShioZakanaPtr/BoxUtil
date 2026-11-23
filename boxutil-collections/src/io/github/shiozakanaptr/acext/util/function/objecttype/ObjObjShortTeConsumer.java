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
public interface ObjObjShortTeConsumer<T, U> extends TeConsumer<T, U, Short> {
    void accept(T t, U u, short v);

    default void accept(T t, U u, Short v) {
        this.accept(t, u, v != null ? v : (short) 0);
    }
}
