package io.github.shiozakanaptr.acext.util.function.shorttype;

import java.util.function.Function;

@FunctionalInterface
public interface ShortToLongFunction extends Function<Short, Long> {
    long apply(short t);

    default Long apply(Short t) {
        return this.apply(t != null ? t : (short) 0);
    }
}
