package org.boxutil.util.function.bytetype;

import java.util.function.Function;

public interface ByteToIntFunction extends Function<Byte, Integer> {
    int apply(byte t);

    default Integer apply(Byte t) {
        return this.apply(t == null ? (byte) 0 : t);
    }
}
