package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortByteBiFunction<R> extends BiFunction<Short, Byte, R> {
    R apply(short t, byte u);

    default R apply(Short t, Byte u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (byte) 0);
    }
}
