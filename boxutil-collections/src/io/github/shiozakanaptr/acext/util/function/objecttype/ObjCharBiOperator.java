package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjCharBiOperator<T> extends BiFunction<T, Character, Character> {
    char apply(T t, char u);

    default Character apply(T t, Character u) {
        return this.apply(t,, u != null ? u : (char) 0);
    }
}
