package org.boxutil.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjByteBiFunction<T, R> extends BiFunction<T, Byte, R> {
    R apply(T t, byte u);

    default R apply(T t, Byte u) {
        return this.apply(t, u == null ? (byte) 0 : u);
    }
}
