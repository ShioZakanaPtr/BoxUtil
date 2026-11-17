package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteIntBiOperator extends BiFunction<Byte, Integer, Integer> {
    int apply(byte t, int u);

    default Integer apply(Byte t, Integer u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (int) 0);
    }
}
