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
public interface ObjFloatBiConsumer<T> extends BiConsumer<T, Float> {
    void accept(T t, float u);

    default void accept(T t, Float u) {
        this.accept(t, u != null ? u : (float) 0);
    }
}
