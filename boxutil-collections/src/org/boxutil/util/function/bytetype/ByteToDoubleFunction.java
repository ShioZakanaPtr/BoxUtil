package org.boxutil.util.function.bytetype;

import java.util.function.Function;

public interface ByteToDoubleFunction extends Function<Byte, Double> {
    double apply(byte t);

    default Double apply(Byte t) {
        return this.apply(t == null ? (byte) 0 : t);
    }
}
