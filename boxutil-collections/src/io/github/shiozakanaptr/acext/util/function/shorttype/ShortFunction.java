package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.Function;

@FunctionalInterface
public interface ShortFunction<R> extends Function<Short, R> {
    R apply(short t);

    default R apply(Short t) {
        return this.apply(t != null ? t : (short) 0);
    }
}
