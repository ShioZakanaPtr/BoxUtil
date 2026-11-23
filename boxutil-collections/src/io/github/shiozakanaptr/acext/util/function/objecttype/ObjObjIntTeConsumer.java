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
public interface ObjObjIntTeConsumer<T, U> extends TeConsumer<T, U, Integer> {
    void accept(T t, U u, int v);

    default void accept(T t, U u, Integer v) {
        this.accept(t, u, v != null ? v : (int) 0);
    }
}
