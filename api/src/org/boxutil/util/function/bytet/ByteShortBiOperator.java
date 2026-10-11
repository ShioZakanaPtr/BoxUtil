package org.boxutil.util.function.bytet;

import java.util.function.BiFunction;

public interface ByteShortBiOperator extends BiFunction<Byte, Short, Short> {
    short apply(byte t, short u);

    default Short apply(Byte t, Short u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (short) 0 : u);
    }
}
