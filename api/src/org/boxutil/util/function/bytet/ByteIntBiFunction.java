package org.boxutil.util.function.bytet;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteIntBiFunction<R> extends BiFunction<Byte, Integer, R> {
    R apply(byte t, int u);

    default R apply(Byte t, Integer u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (int) 0 : u);
    }
}
