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
public interface ShortByteBiOperator extends BiFunction<Short, Byte, Byte> {
    byte apply(short t, byte u);

    default Byte apply(Short t, Byte u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (byte) 0);
    }
}
