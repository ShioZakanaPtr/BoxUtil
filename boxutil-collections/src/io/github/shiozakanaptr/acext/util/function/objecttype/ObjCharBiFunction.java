package io.github.shiozakanaptr.acext.util.function.objecttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ObjCharBiFunction<T, R> extends BiFunction<T, Character, R> {
    R apply(T t, char u);

    default R apply(T t, Character u) {
        return this.apply(t, u != null ? u : (char) 0);
    }
}
