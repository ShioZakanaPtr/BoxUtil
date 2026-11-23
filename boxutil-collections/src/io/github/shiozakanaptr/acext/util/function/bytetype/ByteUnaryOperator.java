package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.UnaryOperator;

/**
 * Type specialization functional interface.
 *
 * @see UnaryOperator
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface ByteUnaryOperator extends UnaryOperator<Byte> {
    byte apply(byte t);

    default Byte apply(Byte t) {
        return this.apply(t != null ? t : (byte) 0);
    }
}
