package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteFloatBiOperator extends BiFunction<Byte, Float, Float> {
    float apply(byte t, float u);

    default Float apply(Byte t, Float u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (float) 0);
    }
}
