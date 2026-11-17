package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteShortBiFunction<R> extends BiFunction<Byte, Short, R> {
    R apply(byte t, short u);

    default R apply(Byte t, Short u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (short) 0);
    }
}
