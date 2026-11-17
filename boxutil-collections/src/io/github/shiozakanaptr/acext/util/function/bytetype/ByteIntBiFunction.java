package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteIntBiFunction<R> extends BiFunction<Byte, Integer, R> {
    R apply(byte t, int u);

    default R apply(Byte t, Integer u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (int) 0);
    }
}
