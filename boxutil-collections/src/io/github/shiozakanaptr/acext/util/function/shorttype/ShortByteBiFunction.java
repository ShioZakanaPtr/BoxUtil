package io.github.shiozakanaptr.acext.util.function.shorttype;

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
public interface ShortByteBiFunction<R> extends BiFunction<Short, Byte, R> {
    R apply(short t, byte u);

    default R apply(Short t, Byte u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (byte) 0);
    }
}
