package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface IntCharBiFunction<R> extends BiFunction<Integer, Character, R> {
    R apply(int t, char u);

    default R apply(Integer t, Character u) {
        return this.apply(t != null ? t : (int) 0, u != null ? u : (char) 0);
    }
}
