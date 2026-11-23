package io.github.shiozakanaptr.acext.util.function.inttype;

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
public interface IntByteBiOperator extends BiFunction<Integer, Byte, Byte> {
    byte apply(int t, byte u);

    default Byte apply(Integer t, Byte u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (byte) 0);
    }
}
