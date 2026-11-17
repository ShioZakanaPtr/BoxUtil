package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Consumer;

@FunctionalInterface
public interface CharConsumer extends Consumer<Character> {
    void accept(char t);

    default void accept(Character t) {
        this.accept(t != null ? t : (char) 0);
    }
}
