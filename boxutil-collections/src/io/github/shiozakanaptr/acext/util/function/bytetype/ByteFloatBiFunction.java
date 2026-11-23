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
public interface ByteFloatBiFunction<R> extends BiFunction<Byte, Float, R> {
    R apply(byte t, float u);

    default R apply(Byte t, Float u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (float) 0);
    }
}
