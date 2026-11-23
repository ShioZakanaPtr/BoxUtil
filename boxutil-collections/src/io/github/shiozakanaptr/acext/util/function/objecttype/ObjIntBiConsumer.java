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
public interface ObjIntBiConsumer<T> extends BiConsumer<T, Integer> {
    void accept(T t, int u);

    default void accept(T t, Integer u) {
        this.accept(t, u != null ? u : (int) 0);
    }
}
