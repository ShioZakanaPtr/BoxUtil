package io.github.shiozakanaptr.acext.util.function.longtype;

import java.util.function.Function;

@FunctionalInterface
public interface LongFunction<R> extends Function<Long, R> {
    R apply(long t);

    default R apply(Long t) {
        return this.apply(t != null ? t : (long) 0);
    }
}
