package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.BiFunction;

@FunctionalInterface
public interface ShortDoubleBiFunction<R> extends BiFunction<Short, Double, R> {
    R apply(short t, double u);

    default R apply(Short t, Double u) {
        return this.apply(t != null ? t : (short) 0, u != null ? u : (double) 0);
    }
}
