package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

/**
 * Type specialization functional interface.
 *
 * @see BiFunction
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteShortBiFunction<R> extends BiFunction<Byte, Short, R> {
    R apply(byte t, short u);

    default R apply(Byte t, Short u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (short) 0);
    }
}
