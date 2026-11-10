package io.github.shiozakanaptr.acext.util.function.inttype;

import java.util.function.Function;

@FunctionalInterface
public interface IntFunction<R> extends Function<Integer, R> {
    R apply(int t);

    default R apply(Integer t) {
        return this.apply(t != null ? t : (int) 0);
    }
}
