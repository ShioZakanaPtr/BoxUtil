package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharShortBiFunction<R> extends BiFunction<Character, Short, R> {
    R apply(char t, short u);

    default R apply(Character t, Short u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (short) 0);
    }
}
