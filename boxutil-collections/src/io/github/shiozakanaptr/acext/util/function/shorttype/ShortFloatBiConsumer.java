package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ShortFloatBiConsumer extends BiConsumer<Short, Float> {
    void accept(short t, float u);

    default void accept(Short t, Float u) {
        this.accept(t != null ? t : (short) 0, u != null ? u : (float) 0);
    }
}
