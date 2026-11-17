package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface DoubleByteBiFunction<R> extends BiFunction<Double, Byte, R> {
    R apply(double t, byte u);

    default R apply(Double t, Byte u) {
        return this.apply(t != null ? t : (double) 0, u != null ? u : (byte) 0);
    }
}
