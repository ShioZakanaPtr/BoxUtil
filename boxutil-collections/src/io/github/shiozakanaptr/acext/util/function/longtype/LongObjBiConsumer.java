package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LongObjBiConsumer<U> extends BiConsumer<Long, U> {
    void accept(long t, U u);

    default void accept(Long t, U u) {
        this.accept(t != null ? t : (long) 0, u);
    }
}
