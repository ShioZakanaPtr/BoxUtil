package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ByteShortBiConsumer extends BiConsumer<Byte, Short> {
    void accept(byte t, short u);

    default void accept(Byte t, Short u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (short) 0);
    }
}
