package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntCharBiOperator extends BiFunction<Integer, Character, Character> {
    char apply(int t, char u);

    default Character apply(Integer t, Character u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (char) 0);
    }
}
