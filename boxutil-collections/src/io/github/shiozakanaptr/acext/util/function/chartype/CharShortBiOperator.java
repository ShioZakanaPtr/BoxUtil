package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharShortBiOperator extends BiFunction<Character, Short, Short> {
    short apply(char t, short u);

    default Short apply(Character t, Short u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (short) 0);
    }
}
