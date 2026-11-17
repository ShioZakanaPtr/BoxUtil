package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteFloatBiFunction<R> extends BiFunction<Byte, Float, R> {
    R apply(byte t, float u);

    default R apply(Byte t, Float u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (float) 0);
    }
}
