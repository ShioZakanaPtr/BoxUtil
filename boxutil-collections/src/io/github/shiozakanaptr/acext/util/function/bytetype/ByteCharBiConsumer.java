package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ByteCharBiConsumer extends BiConsumer<Byte, Character> {
    void accept(byte t, char u);

    default void accept(Byte t, Character u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (char) 0);
    }
}
