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
public interface ByteObjBiConsumer<U> extends BiConsumer<Byte, U> {
    void accept(byte t, U u);

    default void accept(Byte t, U u) {
        this.accept(t != null ? t : (byte) 0, u);
    }
}
