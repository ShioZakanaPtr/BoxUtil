package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.Consumer;

@FunctionalInterface
public interface ShortConsumer extends Consumer<Short> {
    void accept(short t);

    default void accept(Short t) {
        this.accept(t != null ? t : (short) 0);
    }
}
