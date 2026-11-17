package io.github.shiozakanaptr.acext.util.function.bytetype;

import java.util.function.Function;

@FunctionalInterface
public interface ByteFunction<R> extends Function<Byte, R> {
    R apply(byte t);

    default R apply(Byte t) {
        return this.apply(t != null ? t : (byte) 0);
    }
}
