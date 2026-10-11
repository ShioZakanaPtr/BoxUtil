package org.boxutil.util.function.intt;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntByteBiFunction<R> extends BiFunction<Integer, Byte, R> {
    R apply(int t, byte u);

    default R apply(Integer t, Byte u) {
        return this.apply(t == null ? (int) 0 : t, u == null ? (byte) 0 : u);
    }
}
