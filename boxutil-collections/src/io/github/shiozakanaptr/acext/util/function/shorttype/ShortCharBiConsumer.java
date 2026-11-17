package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ShortCharBiConsumer extends BiConsumer<Short, Character> {
    void accept(short t, char u);

    default void accept(Short t, Character u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (char) 0);
    }
}
