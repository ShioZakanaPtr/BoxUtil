package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortCharBiOperator extends BiFunction<Short, Character, Character> {
    char apply(short t, char u);

    default Character apply(Short t, Character u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (char) 0);
    }
}
