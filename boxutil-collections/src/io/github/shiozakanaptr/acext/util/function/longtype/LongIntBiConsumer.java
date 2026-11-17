package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LongIntBiConsumer extends BiConsumer<Long, Integer> {
    void accept(long t, int u);

    default void accept(Long t, Integer u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (int) 0);
    }
}
