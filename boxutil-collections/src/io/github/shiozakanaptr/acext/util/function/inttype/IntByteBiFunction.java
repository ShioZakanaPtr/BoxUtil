package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntByteBiFunction<R> extends BiFunction<Integer, Byte, R> {
    R apply(int t, byte u);

    default R apply(Integer t, Byte u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (byte) 0);
    }
}
