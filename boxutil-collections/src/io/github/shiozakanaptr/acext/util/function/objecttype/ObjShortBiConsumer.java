package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiConsumer;

/**
 * Type specialization functional interface.
 *
 * @see BiConsumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ObjShortBiConsumer<T> extends BiConsumer<T, Short> {
    void accept(T t, short u);

    default void accept(T t, Short u) {
        this.accept(t, u != null ? u : (short) 0);
    }
}
