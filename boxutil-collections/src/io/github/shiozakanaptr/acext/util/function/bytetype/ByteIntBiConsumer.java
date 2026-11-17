package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ByteIntBiConsumer extends BiConsumer<Byte, Integer> {
    void accept(byte t, int u);

    default void accept(Byte t, Integer u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (int) 0);
    }
}
