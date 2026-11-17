package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface DoubleObjBiConsumer<U> extends BiConsumer<Double, U> {
    void accept(double t, U u);

    default void accept(Double t, U u) {
        this.accept(t != null ? t : (double) 0, u);
    }
}
