package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharCharBiOperator extends BiFunction<Character, Character, Character> {
    char apply(char t, char u);

    default Character apply(Character t, Character u) {
        return this.apply(t != null ? t : (char) 0, u != null ? u : (char) 0);
    }
}
