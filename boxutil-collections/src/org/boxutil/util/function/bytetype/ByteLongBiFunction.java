package org.boxutil.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteLongBiFunction<R> extends BiFunction<Byte, Long, R> {
    R apply(byte t, long u);

    default R apply(Byte t, Long u) {
        return this.apply(t == null ? (byte) 0 : t, u == null ? (long) 0 : u);
    }
}
