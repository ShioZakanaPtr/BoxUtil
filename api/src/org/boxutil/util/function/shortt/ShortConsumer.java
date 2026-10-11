package org.boxutil.util.function.shortt;

import java.util.function.Consumer;

public interface ShortConsumer extends Consumer<Short> {
    void accept(short t);

    default void accept(Short t) {
        this.accept(t == null ? (short) 0 : t);
    }
}
