package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.Consumer;

@FunctionalInterface
public interface DoubleConsumer extends Consumer<Double> {
    void accept(double t);

    default void accept(Double t) {
        this.accept(t != null ? t : (double) 0);
    }
}
