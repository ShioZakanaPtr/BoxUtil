package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CharLongBiConsumer extends BiConsumer<Character, Long> {
    void accept(char t, long u);

    default void accept(Character t, Long u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (long) 0);
    }
}
