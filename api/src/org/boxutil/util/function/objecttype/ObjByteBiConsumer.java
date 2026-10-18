package org.boxutil.util.function.objecttype;

import java.util.function.BiConsumer;

public interface ObjByteBiConsumer<T> extends BiConsumer<T, Byte> {
    void accept(T t, byte u);

    default void accept(T t, Byte u) {
        this.accept(t, u == null ? (byte) 0 : u);
    }
}
