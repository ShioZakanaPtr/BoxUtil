package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.Consumer;

/**
 * Type specialization functional interface.
 *
 * @see Consumer
 *
 * @since 1.0.0
 * @author ShioZakanaPtr
 */
@FunctionalInterface
public interface FloatConsumer extends Consumer<Float> {
    void accept(float t);

    default void accept(Float t) {
        this.accept(t != null ? t : (float) 0);
    }
}
