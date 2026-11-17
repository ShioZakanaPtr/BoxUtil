package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongCharBiOperator extends BiFunction<Long, Character, Character> {
    char apply(long t, char u);

    default Character apply(Long t, Character u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (char) 0);
    }
}
