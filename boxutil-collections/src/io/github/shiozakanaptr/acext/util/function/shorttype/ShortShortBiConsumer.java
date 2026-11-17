package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ShortShortBiConsumer extends BiConsumer<Short, Short> {
    void accept(short t, short u);

    default void accept(Short t, Short u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (short) 0);
    }
}
