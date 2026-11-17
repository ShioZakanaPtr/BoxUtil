package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LongLongBiConsumer extends BiConsumer<Long, Long> {
    void accept(long t, long u);

    default void accept(Long t, Long u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (long) 0);
    }
}
