package io.github.shiozakanaptr.acext.util.function.doubletype;

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
public interface DoubleByteBiOperator extends BiFunction<Double, Byte, Byte> {
    byte apply(double t, byte u);

    default Byte apply(Double t, Byte u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (byte) 0);
    }
}
