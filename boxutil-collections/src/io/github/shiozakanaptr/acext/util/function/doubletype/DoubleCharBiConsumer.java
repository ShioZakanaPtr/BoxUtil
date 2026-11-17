package io.github.shiozakanaptr.acext.util.function.doubletype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface DoubleCharBiConsumer extends BiConsumer<Double, Character> {
    void accept(double t, char u);

    default void accept(Double t, Character u) {
        this.accept(t != null ? t : (double) 0, u != null ? u : (char) 0);
    }
}
