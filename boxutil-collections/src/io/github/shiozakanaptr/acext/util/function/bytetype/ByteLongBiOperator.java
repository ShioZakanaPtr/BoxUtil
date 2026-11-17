package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteLongBiOperator extends BiFunction<Byte, Long, Long> {
    long apply(byte t, long u);

    default Long apply(Byte t, Long u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (long) 0);
    }
}
