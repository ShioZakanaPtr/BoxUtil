package io.github.shiozakanaptr.acext.util.function.floattype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface FloatCharBiConsumer extends BiConsumer<Float, Character> {
    void accept(float t, char u);

    default void accept(Float t, Character u) {
        this.accept(t != null ? t : (float) 0, u != null ? u : (char) 0);
    }
}
