package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CharCharBiConsumer extends BiConsumer<Character, Character> {
    void accept(char t, char u);

    default void accept(Character t, Character u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (char) 0);
    }
}
