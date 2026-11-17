package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ShortObjBiConsumer<U> extends BiConsumer<Short, U> {
    void accept(short t, U u);

    default void accept(Short t, U u) {
        this.accept(t != null ? t : (short) 0, u);
    }
}
