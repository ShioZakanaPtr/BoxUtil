package org.boxutil.util.function.bytet;

import java.util.function.Function;

public interface ByteFunction<R> extends Function<Byte, R> {
    R apply(byte t);

    default R apply(Byte t) {
        return this.apply(t == null ? (byte) 0 : t);
    }
}
