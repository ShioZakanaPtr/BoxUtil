package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LongShortBiConsumer extends BiConsumer<Long, Short> {
    void accept(long t, short u);

    default void accept(Long t, Short u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (short) 0);
    }
}
