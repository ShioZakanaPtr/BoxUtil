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
public interface ByteByteBiOperator extends BiFunction<Byte, Byte, Byte> {
    byte apply(byte t, byte u);

    default Byte apply(Byte t, Byte u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (byte) 0);
    }
}
