package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ObjCharBiConsumer<T> extends BiConsumer<T, Character> {
    void accept(T t, char u);

    default void accept(T t, Character u) {
        this.accept(t, u != null ? u : (char) 0);
    }
}
