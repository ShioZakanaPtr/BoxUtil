package org.boxutil.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteByteBiFunction<R> extends BiFunction<Byte, Byte, R> {
    R apply(byte t, byte u);

    default R apply(Byte t, Byte u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (byte) 0 : u);
    }
}
