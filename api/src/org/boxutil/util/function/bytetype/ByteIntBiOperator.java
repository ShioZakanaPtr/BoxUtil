package org.boxutil.util.function.bytetype;

import java.util.function.BiFunction;

public interface ByteIntBiOperator extends BiFunction<Byte, Integer, Integer> {
    int apply(byte t, int u);

    default Integer apply(Byte t, Integer u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (int) 0 : u);
    }
}
