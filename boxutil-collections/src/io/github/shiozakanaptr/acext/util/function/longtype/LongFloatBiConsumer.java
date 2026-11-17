package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LongFloatBiConsumer extends BiConsumer<Long, Float> {
    void accept(long t, float u);

    default void accept(Long t, Float u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (float) 0);
    }
}
