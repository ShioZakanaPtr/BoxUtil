package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface DoubleByteBiConsumer extends BiConsumer<Double, Byte> {
    void accept(double t, byte u);

    default void accept(Double t, Byte u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (byte) 0);
    }
}
