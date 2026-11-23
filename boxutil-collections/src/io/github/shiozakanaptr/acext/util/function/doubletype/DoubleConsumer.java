package io.github.shiozakanaptr.acext.util.function.doubletype;

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
public interface DoubleConsumer extends Consumer<Double> {
    void accept(double t);

    default void accept(Double t) {
        this.accept(t != null ? t : (double) 0);
    }
}
