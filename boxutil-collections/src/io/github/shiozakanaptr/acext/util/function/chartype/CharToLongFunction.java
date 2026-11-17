package io.github.shiozakanaptr.acext.util.function.chartype;

import java.util.function.Function;

@FunctionalInterface
public interface CharToLongFunction extends Function<Character, Long> {
    long apply(char t);

    default Long apply(Character t) {
        return this.apply(t != null ? t : (char) 0);
    }
}
