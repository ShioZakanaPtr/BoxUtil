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
public interface LongObjBiConsumer<U> extends BiConsumer<Long, U> {
    void accept(long t, U u);

    default void accept(Long t, U u) {
        this.accept(t != null ? t : (long) 0, u);
    }
}
