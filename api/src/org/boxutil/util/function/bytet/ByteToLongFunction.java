package org.boxutil.util.function.bytet;

import java.util.function.Function;

public interface ByteToLongFunction extends Function<Byte, Long> {
    long apply(byte t);

    default Long apply(Byte t) {
        return this.apply(t == null ? (byte) 0 : t);
    }
}
