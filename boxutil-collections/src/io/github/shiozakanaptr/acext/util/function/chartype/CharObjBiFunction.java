package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface CharObjBiFunction<U, R> extends BiFunction<Character, U, R> {
    R apply(char t, U u);

    default R apply(Character t, U u) {
        return this.apply(t != null ? t : (char) 0, u);
    }
}
