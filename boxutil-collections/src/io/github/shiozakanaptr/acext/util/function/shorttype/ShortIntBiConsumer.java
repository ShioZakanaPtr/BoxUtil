package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ShortIntBiConsumer extends BiConsumer<Short, Integer> {
    void accept(short t, int u);

    default void accept(Short t, Integer u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (int) 0);
    }
}
