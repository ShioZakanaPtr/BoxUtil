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
public interface ObjLongBiConsumer<T> extends BiConsumer<T, Long> {
    void accept(T t, long u);

    default void accept(T t, Long u) {
        this.accept(t, u != null ? u : (long) 0);
    }
}
