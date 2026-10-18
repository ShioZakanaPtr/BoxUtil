package org.boxutil.util.function.shorttype;

import java.util.function.BiFunction;

public interface ShortByteBiOperator extends BiFunction<Short, Byte, Byte> {
    byte apply(short t, byte u);

    default Byte apply(Short t, Byte u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (byte) 0 : u);
    }
}
