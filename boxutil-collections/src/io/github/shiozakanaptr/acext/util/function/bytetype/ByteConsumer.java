package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.Consumer;

/**
 * Type specialization functional interface.
 *
 * @see Consumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteConsumer extends Consumer<Byte> {
    void accept(byte t);

    default void accept(Byte t) {
        this.accept(t != null ? t : (byte) 0);
    }
}
