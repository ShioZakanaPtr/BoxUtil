package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CharShortBiConsumer extends BiConsumer<Character, Short> {
    void accept(char t, short u);

    default void accept(Character t, Short u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (short) 0);
    }
}
