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
public interface ObjCharBiConsumer<T> extends BiConsumer<T, Character> {
    void accept(T t, char u);

    default void accept(T t, Character u) {
        this.accept(t, u != null ? u : (char) 0);
    }
}
