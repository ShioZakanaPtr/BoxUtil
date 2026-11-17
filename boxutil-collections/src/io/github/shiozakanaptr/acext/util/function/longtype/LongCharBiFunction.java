package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface LongCharBiFunction<R> extends BiFunction<Long, Character, R> {
    R apply(long t, char u);

    default R apply(Long t, Character u) {
        return this.apply(t != null ? t : (long) 0, u != null ? u : (char) 0);
    }
}
