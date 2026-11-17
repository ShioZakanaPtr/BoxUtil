package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CharDoubleBiConsumer extends BiConsumer<Character, Double> {
    void accept(char t, double u);

    default void accept(Character t, Double u) {
        this.accept(t != null ? t : (char) 0, u != null ? u : (double) 0);
    }
}
