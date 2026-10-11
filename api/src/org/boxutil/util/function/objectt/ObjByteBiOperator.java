package org.boxutil.util.function.objectt;

import java.util.function.BiFunction;

public interface ObjByteBiOperator<T> extends BiFunction<T, Byte, Byte> {
    byte apply(T t, byte u);

    default Byte apply(T t, Byte u) {
        return this.apply(t, u == null ? (byte) 0 : u);
    }
}
