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
public interface ObjByteBiConsumer<T> extends BiConsumer<T, Byte> {
    void accept(T t, byte u);

    default void accept(T t, Byte u) {
        this.accept(t, u != null ? u : (byte) 0);
    }
}
