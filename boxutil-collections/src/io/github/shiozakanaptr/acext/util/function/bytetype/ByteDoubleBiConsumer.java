package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiConsumer;

/**
 * Type specialization functional interface.
 *
 * @see BiConsumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteDoubleBiConsumer extends BiConsumer<Byte, Double> {
    void accept(byte t, double u);

    default void accept(Byte t, Double u) {
        this.accept(t != null ? t : (byte) 0, u != null ? u : (double) 0);
    }
}
