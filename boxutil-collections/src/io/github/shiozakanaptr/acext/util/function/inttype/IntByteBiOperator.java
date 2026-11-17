package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntByteBiOperator extends BiFunction<Integer, Byte, Byte> {
    byte apply(int t, byte u);

    default Byte apply(Integer t, Byte u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (byte) 0);
    }
}
