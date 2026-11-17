package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface IntCharBiConsumer extends BiConsumer<Integer, Character> {
    void accept(int t, char u);

    default void accept(Integer t, Character u) {
        this.accept(t != null ? t : (int) 0, u != null ? u : (char) 0);
    }
}
