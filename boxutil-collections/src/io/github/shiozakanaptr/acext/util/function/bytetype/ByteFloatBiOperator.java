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
public interface ByteFloatBiOperator extends BiFunction<Byte, Float, Float> {
    float apply(byte t, float u);

    default Float apply(Byte t, Float u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (float) 0);
    }
}
