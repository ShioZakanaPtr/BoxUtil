package org.boxutil.util.function.inttype;

import java.util.function.BiFunction;

public interface IntByteBiOperator extends BiFunction<Integer, Byte, Byte> {
    byte apply(int t, byte u);

    default Byte apply(Integer t, Byte u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (byte) 0 : u);
    }
}
