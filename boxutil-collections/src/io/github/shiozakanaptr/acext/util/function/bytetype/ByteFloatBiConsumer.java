package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ByteFloatBiConsumer extends BiConsumer<Byte, Float> {
    void accept(byte t, float u);

    default void accept(Byte t, Float u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (float) 0);
    }
}
