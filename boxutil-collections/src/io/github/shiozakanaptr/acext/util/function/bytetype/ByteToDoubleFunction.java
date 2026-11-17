package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.Function;

@FunctionalInterface
public interface ByteToDoubleFunction extends Function<Byte, Double> {
    double apply(byte t);

    default Double apply(Byte t) {
        return this.apply(t != null ? t : (byte) 0);
    }
}
