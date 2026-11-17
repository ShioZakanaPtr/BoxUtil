package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.Function;

@FunctionalInterface
public interface ShortToDoubleFunction extends Function<Short, Double> {
    double apply(short t);

    default Double apply(Short t) {
        return this.apply(t != null ? t : (short) 0);
    }
}
