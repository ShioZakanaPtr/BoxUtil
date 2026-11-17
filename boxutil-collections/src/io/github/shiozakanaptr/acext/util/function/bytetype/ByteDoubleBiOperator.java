package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteDoubleBiOperator extends BiFunction<Byte, Double, Double> {
    double apply(byte t, double u);

    default Double apply(Byte t, Double u) {
        return this.apply(t != null ? t : (byte) 0, u != null ? u : (double) 0);
    }
}
