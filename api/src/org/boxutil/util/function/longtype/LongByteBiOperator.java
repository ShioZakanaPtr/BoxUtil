package org.boxutil.util.function.longtype;

import java.util.function.BiFunction;

public interface LongByteBiOperator extends BiFunction<Long, Byte, Byte> {
    byte apply(long t, byte u);

    default Byte apply(Long t, Byte u) {
        return this.apply(t == null ? (long) 0 : t, u == null ? (byte) 0 : u);
    }
}
