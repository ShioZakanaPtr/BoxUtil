package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatByteBiFunction<R> extends BiFunction<Float, Byte, R> {
    R apply(float t, byte u);

    default R apply(Float t, Byte u) {
        return this.apply(t != null ? t : (float) 0, u != null ? u : (byte) 0);
    }
}
