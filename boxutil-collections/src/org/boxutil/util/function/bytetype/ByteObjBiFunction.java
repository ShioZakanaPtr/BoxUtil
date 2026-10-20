package org.boxutil.util.function.bytetype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ByteObjBiFunction<U, R> extends BiFunction<Byte, U, R> {
    R apply(byte t, U u);

    default R apply(Byte t, U u) {
        return this.apply(t == null ? (byte) 0 : t, u);
    }
}
