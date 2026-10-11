package org.boxutil.util.function.shortt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortByteBiFunction<R> extends BiFunction<Short, Byte, R> {
    R apply(short t, byte u);

    default R apply(Short t, Byte u) {
        return this.apply(t == null ? (short) 0 : t, u == null ? (byte) 0 : u);
    }
}
