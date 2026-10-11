package org.boxutil.util.function.bytet;

import java.util.function.Consumer;

public interface ByteConsumer extends Consumer<Byte> {
    void accept(byte t);

    default void accept(Byte t) {
        this.accept(t == null ? (byte) 0 : t);
    }
}
