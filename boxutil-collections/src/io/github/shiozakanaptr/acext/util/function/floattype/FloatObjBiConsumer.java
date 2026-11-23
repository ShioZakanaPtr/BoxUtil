package io.github.shiozakanaptr.acext.util.function.floattype;

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
public interface FloatObjBiConsumer<U> extends BiConsumer<Float, U> {
    void accept(float t, U u);

    default void accept(Float t, U u) {
        this.accept(t != null ? t : (float) 0, u);
    }
}
