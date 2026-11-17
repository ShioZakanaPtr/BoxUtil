package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CharByteBiConsumer extends BiConsumer<Character, Byte> {
    void accept(char t, byte u);

    default void accept(Character t, Byte u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (byte) 0);
    }
}
