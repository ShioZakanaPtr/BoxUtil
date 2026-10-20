package org.boxutil.util.function.bytetype;

import java.util.function.BiFunction;

public interface ByteLongBiOperator extends BiFunction<Byte, Long, Long> {
    long apply(byte t, long u);

    default Long apply(Byte t, Long u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (long) 0 : u);
    }
}
