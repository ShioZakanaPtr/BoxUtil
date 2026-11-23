package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiConsumer;

/**
 * Type specialization functional interface.
 *
 * @see BiConsumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface LongFloatBiConsumer extends BiConsumer<Long, Float> {
    void accept(long t, float u);

    default void accept(Long t, Float u) {
        this.accept(t != null ? t : (long) 0, u != null ? u : (float) 0);
    }
}
