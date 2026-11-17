package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongByteBiFunction<R> extends BiFunction<Long, Byte, R> {
    R apply(long t, byte u);

    default R apply(Long t, Byte u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (byte) 0);
    }
}
