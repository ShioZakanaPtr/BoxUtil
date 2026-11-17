package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteDoubleBiFunction<R> extends BiFunction<Byte, Double, R> {
    R apply(byte t, double u);

    default R apply(Byte t, Double u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (double) 0);
    }
}
