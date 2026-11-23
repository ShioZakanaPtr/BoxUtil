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
public interface ByteShortBiOperator extends BiFunction<Byte, Short, Short> {
    short apply(byte t, short u);

    default Short apply(Byte t, Short u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (short) 0);
    }
}
