package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CharObjBiConsumer<U> extends BiConsumer<Character, U> {
    void accept(char t, U u);

    default void accept(Character t, U u) {
        this.accept(t != null ? t : (char) 0, u);
    }
}
