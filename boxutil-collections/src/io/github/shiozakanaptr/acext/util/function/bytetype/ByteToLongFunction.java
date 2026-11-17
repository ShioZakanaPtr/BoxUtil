package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.Function;

@FunctionalInterface
public interface ByteToLongFunction extends Function<Byte, Long> {
    long apply(byte t);

    default Long apply(Byte t) {
        return this.apply(t != null ? t : (byte) 0);
    }
}
