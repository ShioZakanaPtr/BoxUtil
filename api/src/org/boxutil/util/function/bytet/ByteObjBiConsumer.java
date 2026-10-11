package org.boxutil.util.function.bytet;

import java.util.function.BiConsumer;

public interface ByteObjBiConsumer<U> extends BiConsumer<Byte, U> {
    void accept(byte t, U u);

    default void accept(Byte t, U u) {
        this.accept(t == null ? (byte) 0 : t, u);
    }
}
